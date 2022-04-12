import {
    HttpErrorResponse,
    HttpEvent,
    HttpHandler,
    HttpHeaders,
    HttpInterceptor,
    HttpRequest,
    HttpResponseBase
  } from '@angular/common/http';
  import { Injectable, Injector } from '@angular/core';
  import { Router } from '@angular/router';
  import { environment } from '@env/environment';
import { KeycloakService } from 'keycloak-angular';
  import { NzNotificationService } from 'ng-zorro-antd/notification';
  import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
  import { catchError, filter, mergeMap, switchMap, take } from 'rxjs/operators';
  
  const CODEMESSAGE: { [key: number]: string } = {
    200: '服务器成功返回请求的数据。',
    201: '新建或修改数据成功。',
    202: '一个请求已经进入后台排队（异步任务）。',
    204: '删除数据成功。',
    400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
    401: '用户没有权限（令牌、用户名、密码错误）。',
    403: '用户得到授权，但是访问是被禁止的。',
    404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
    406: '请求的格式不可得。',
    410: '请求的资源被永久删除，且不会再得到的。',
    422: '当创建一个对象时，发生一个验证错误。',
    500: '服务器发生错误，请检查服务器。',
    502: '网关错误。',
    503: '服务不可用，服务器暂时过载或维护。',
    504: '网关超时。'
  };
  
  /**
   * 默认HTTP拦截器
   */
  @Injectable()
  export class DefaultInterceptor implements HttpInterceptor {

    constructor(private injector: Injector, private readonly keycloak: KeycloakService) {
    }
  
    private get notification(): NzNotificationService {
      return this.injector.get(NzNotificationService);
    }
  
    private goTo(url: string): void {
      setTimeout(() => this.injector.get(Router).navigateByUrl(url));
    }
  
    private checkStatus(ev: HttpResponseBase): void {
      if ((ev.status >= 200 && ev.status < 300) || ev.status === 401) {
        return;
      }
  
      const errortext = CODEMESSAGE[ev.status] || ev.statusText;
      this.notification.error(`业务异常`, errortext);
    }
  
    private toLogin(): void {
      this.notification.error(`未登录或登录已过期，请重新登录。`, ``);
      this.keycloak.login();
    }
  
    private handleData(ev: HttpResponseBase, req: HttpRequest<any>, next: HttpHandler): Observable<any> {
      this.checkStatus(ev);
      switch (ev.status) {
        case 200:
          break;
        case 401:
          this.toLogin();
          break;
        case 403:
        case 404:
        case 500:
            const errortext = CODEMESSAGE[ev.status] || ev.statusText;
            this.notification.error(`内部异常 ${ev.status}: ${ev.url}`, errortext);
          break;
        default:
          if (ev instanceof HttpErrorResponse) {
            console.warn('未可知错误',ev);
          }
          break;
      }
      if (ev instanceof HttpErrorResponse) {
        return throwError(() => ev);
      } else {
        return of(ev);
      }
    }
  
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      // 统一加上服务端前缀
      let url = req.url;
      if (!url.startsWith('https://') && !url.startsWith('http://')) {
        const { prefixUrl } = environment.api;
        url = prefixUrl + (prefixUrl.endsWith('/') && url.startsWith('/') ? url.substring(1) : url);
      }
  
      const newReq = req.clone({ url });
      return next.handle(newReq).pipe(
        mergeMap(ev => {
          // 允许统一对请求错误处理
          if (ev instanceof HttpResponseBase) {
            return this.handleData(ev, newReq, next);
          }
          // 若一切都正常，则后续操作
          return of(ev);
        }),
        catchError((err: HttpErrorResponse) => this.handleData(err, newReq, next))
      );
    }
  }
  