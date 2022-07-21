package kopo.poly.controller;

import kopo.poly.dto.CategoryDTO;
import kopo.poly.service.ICategoryService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "cate")
public class CategoryCotroller {

    // Map 객체를 사용한 데이터 처리
    @Resource(name = "CategoryService")
    private ICategoryService categoryService;

    /**
     * 현재 태크의 그룹(부모 태그 가져오기)
     */
    @GetMapping(value = "getParentTags")
    public List<CategoryDTO> getParentTags(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getParentTags Start!");

        // 그룹을 찾을 태그명
        String tag = CmmUtil.nvl(request.getParameter("tag"));

        CategoryDTO pDTO = new CategoryDTO();

        pDTO.setTag(tag);

        List<CategoryDTO> rList = categoryService.getParentTags(pDTO);

        if (rList == null) {
            rList = new ArrayList<>();

        }

        log.info(this.getClass().getName() + ".getParentTags End!");

        return rList;
    }

    /**
     * 현재 태그의 자식 태그 조회하기
     */
    @GetMapping(value = "getChildTags")
    public List<CategoryDTO> getChildTags(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getChildTags Start!");

        // 그룹을 찾을 태그명
        String tag = CmmUtil.nvl(request.getParameter("tag"));

        CategoryDTO pDTO = new CategoryDTO();

        pDTO.setTag(tag);

        List<CategoryDTO> rList = categoryService.getChildTags(pDTO);

        if (rList == null) {
            rList = new ArrayList<>();

        }

        log.info(this.getClass().getName() + ".getChildTags End!");

        return rList;
    }

    /**
     * NLP 카테고리에 등록된 메뉴만 조회하기
     */
    @GetMapping(value = "getCategory")
    public CategoryDTO getCategory(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getCategory Start!");

        // 그룹을 찾을 태그명
        String tag = CmmUtil.nvl(request.getParameter("tag"));

        log.info("Tag : " + tag);
        CategoryDTO pDTO = new CategoryDTO();

        pDTO.setTag(tag);

        CategoryDTO rDTO = categoryService.getCategory(pDTO);

        if (rDTO == null) {
            rDTO = new CategoryDTO();

        }

        log.info(this.getClass().getName() + ".getCategory End!");

        return rDTO;
    }

    /**
     * NLP 카테고리에 등록된 메뉴만 조회하기
     */
    @GetMapping(value = "getTagCategory")
    public List<CategoryDTO> getTagCategory(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getTagCategory Start!");

        // 그룹을 찾을 태그명
        String tag = CmmUtil.nvl(request.getParameter("tag"));

        log.info("Tag : " + tag);
        CategoryDTO pDTO = new CategoryDTO();

        pDTO.setTag(tag);

        List<CategoryDTO> rList = categoryService.getTagCategory(pDTO);

        if (rList == null) {
            rList = new ArrayList<>();

        }

        log.info(this.getClass().getName() + ".getTagCategory End!");

        return rList;
    }

    /**
     * NLP 카테고리에 등록된 메뉴만 조회하기
     */
    @GetMapping(value = "getCategoryAll")
    public List<CategoryDTO> getCategoryAll(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getCategoryAll Start!");

        List<CategoryDTO> rList = categoryService.getCategoryAll();

        if (rList == null) {
            rList = new ArrayList<>();

        }

        log.info(this.getClass().getName() + ".getCategoryAll End!");

        return rList;
    }
    /**
     * NLP 카테고리에 등록된 메뉴만 조회하기
     */
    @GetMapping(value = "getTagCategoryAll")
    public List<CategoryDTO> getTagCategoryAll(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getTagCategoryAll Start!");

        List<CategoryDTO> rList = categoryService.getTagCategoryAll();

        if (rList == null) {
            rList = new ArrayList<>();

        }

        log.info(this.getClass().getName() + ".getTagCategoryAll End!");

        return rList;
    }


}
