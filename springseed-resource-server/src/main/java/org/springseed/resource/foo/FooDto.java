package org.springseed.resource.foo;

import lombok.Getter;

/**
 * 数据传输对象
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Getter
public class FooDto {
    private String id;
    private String name;

    private boolean nameUpdate = false;

    public FooDto() {
    }

    public FooDto(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.nameUpdate = true;
        this.name = name;
    } 

    public void copyTo(final Foo foo) {
        if (nameUpdate) {
            foo.setName(this.getName());
        }
    }
}
