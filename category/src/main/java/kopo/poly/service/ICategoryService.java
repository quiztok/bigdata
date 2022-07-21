package kopo.poly.service;

import kopo.poly.dto.CategoryDTO;

import java.util.List;

public interface ICategoryService {

    List<CategoryDTO> getParentTags(CategoryDTO pDTO) throws Exception;

    List<CategoryDTO> getChildTags(CategoryDTO pDTO) throws Exception;

    CategoryDTO getCategory(CategoryDTO pDTO) throws Exception;

    List<CategoryDTO> getTagCategory(CategoryDTO pDTO) throws Exception;

    List<CategoryDTO> getCategoryAll() throws Exception;

    List<CategoryDTO> getTagCategoryAll() throws Exception;

}
