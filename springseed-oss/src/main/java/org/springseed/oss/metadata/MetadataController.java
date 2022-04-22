package org.springseed.oss.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 元数据API接口
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/metadatas")
public class MetadataController {
    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private MetadataQueryService metadataQueryService;
    @Autowired
    private ModelMapper modelMapper;

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
