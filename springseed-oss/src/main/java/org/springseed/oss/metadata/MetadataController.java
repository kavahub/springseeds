package org.springseed.oss.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * 元数据API接口
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oss/metadatas")
public class MetadataController {
    private final MetadataRepository metadataRepository;
    private final MetadataQueryService metadataQueryService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "/{id}")
    public MetadataDto getById(@PathVariable String id) {
        return this.metadataQueryService.findById(id).map(metadata -> modelMapper.map(metadata, MetadataDto.class))
                .get();
    }

    @GetMapping(value = "/ids")
    public List<MetadataDto> getByIds(@RequestParam List<String> ids) {
        return metadataRepository.findAllById(ids)
                .stream()
                .map(e -> modelMapper.map(e, MetadataDto.class))
                .collect(Collectors.toList());
    }
}
