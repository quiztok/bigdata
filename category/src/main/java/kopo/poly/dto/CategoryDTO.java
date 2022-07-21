package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CategoryDTO {

    @NonNull
    private String tag;

    private String category;
    private String parentCategory;
    private Long depth;
    private List<String> tags;
    private List<String> tree;

}
