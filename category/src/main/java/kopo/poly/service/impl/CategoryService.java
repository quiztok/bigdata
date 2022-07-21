package kopo.poly.service.impl;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import kopo.poly.dto.CategoryDTO;
import kopo.poly.service.ICategoryService;
import kopo.poly.service.comm.AbstractMongoDBComon;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("CategoryService")
public class CategoryService extends AbstractMongoDBComon implements ICategoryService {

    @Override
    public List<CategoryDTO> getParentTags(CategoryDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getParentTags Start!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<CategoryDTO> rList = new LinkedList<>();

        String colNm = "NLP_CLUSTER_DICTIONARY";

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        // 조회 결과 중 출력할 컬럼들(SQL의 SELECT절과 FROM절 가운데 컬럼들과 유사함)
        Document projection = new Document();
        projection.append("tag", "$tag");
        projection.append("tags", "$tags");
        projection.append("_id", 0);

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(new Document("tags", CmmUtil.nvl(pDTO.getTag()))).projection(projection);

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();

            }

            String tag = CmmUtil.nvl(doc.getString("tag"));

            CategoryDTO rDTO = new CategoryDTO();
            rDTO.setTag(tag);

            rList.add(rDTO);

            rDTO = null;
        }

        log.info(this.getClass().getName() + ".getParentTags End!");

        return rList;
    }

    @Override
    public List<CategoryDTO> getChildTags(CategoryDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getChildTags Start!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<CategoryDTO> rList = new LinkedList<>();

        String colNm = "NLP_CLUSTER_DICTIONARY";

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        // 조회 결과 중 출력할 컬럼들(SQL의 SELECT절과 FROM절 가운데 컬럼들과 유사함)
        Document projection = new Document();
        projection.append("tag", "$tag");
        projection.append("tags", "$tags");
        projection.append("_id", 0);

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(new Document("tag", CmmUtil.nvl(pDTO.getTag()))).projection(projection);

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();

            }

            List<String> tags = doc.getList("tags", String.class, new LinkedList<String>());

            CategoryDTO rDTO = new CategoryDTO();
            rDTO.setTags(tags);

            rList.add(rDTO);

            rDTO = null;
        }

        log.info(this.getClass().getName() + ".getChildTags End!");

        return rList;
    }

    @Override
    public CategoryDTO getCategory(CategoryDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getCategory Start!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<CategoryDTO> rList = new LinkedList<>();

        // 트리 결과를 저장할 객체
        List<String> treeList = new LinkedList<>();

        String colNm = "NLP_CATEGORY";

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$match", new Document()
                                .append("tag", CmmUtil.nvl(pDTO.getTag()))
                        ),
                new Document()
                        .append("$graphLookup", new Document()
                                .append("from", colNm)
                                .append("startWith", "$parent")
                                .append("connectFromField", "parent")
                                .append("connectToField", "tag")
                                .append("as", "tree")
                                .append("depthField", "depth")
                        )
        );

        AggregateIterable<Document> rs = col.aggregate(pipeline).allowDiskUse(true);
        Iterator<Document> cursor = rs.iterator();

        Document doc = rs.first(); // 첫번째 레코드 가져오기

        if (doc == null) {
            doc = new Document();

        }

        log.info("doc : " + doc);
        String curentTag = CmmUtil.nvl(doc.getString("tag")); // 가져올 태그
        String parentTag = CmmUtil.nvl(doc.getString("parent")); // 부모 태그

        // 트리값 가져오기
        List<Document> tree = doc.getList("tree", Document.class, new LinkedList<>());

        // 트리 값 정렬
        // 나이(Integer) 내림차순
        Collections.sort(tree, (Comparator<Document>) (o1, o2) -> {
            Long depth1 = o1.getLong("depth");
            Long depth2 = o2.getLong("depth");
            return depth2.compareTo(depth1);
        });

        // 카테고리 순서대로 넣기
        for (Document tDoc : tree) {
            String treeTag = CmmUtil.nvl(tDoc.getString("tag"));
            treeList.add(treeTag);
        }

        treeList.add(curentTag); // 자기 자신도 넣기

        CategoryDTO rDTO = new CategoryDTO();
        rDTO.setCategory(curentTag);
//        rDTO.setParentCategory(parentTag);
        rDTO.setTree(treeList);

        treeList = null;
        rs = null;
        pipeline = null;
        col = null;


        log.info(this.getClass().getName() + ".getCategory End!");

        return rDTO;

    }

    @Override
    public List<CategoryDTO> getTagCategory(CategoryDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getTagCategory Start!");

        String tag = CmmUtil.nvl(pDTO.getTag());

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<CategoryDTO> rList = new LinkedList<>();

        String colNm = "NLP_CLUSTER_DICTIONARY";

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        // 조회 결과 중 출력할 컬럼들(SQL의 SELECT절과 FROM절 가운데 컬럼들과 유사함)
        Document projection = new Document();
        projection.append("tag", "$tag");
        projection.append("tags", "$tags");
        projection.append("_id", 0);

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(new Document("tags", tag)).projection(projection);

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();

            }

            String parentTag = CmmUtil.nvl(doc.getString("tag"));

//            log.info("tag : " + tag);
//
//            CategoryDTO tDTO = new CategoryDTO();
//            tDTO.setTag(parentTag);

            // Tree 가져오기
            CategoryDTO rDTO = this.getCategory(new CategoryDTO(parentTag));

//            tDTO = null;

            rDTO.setTag(tag);
//            rDTO.setParentCategory(parentTag);
            rDTO.setParentCategory(null);
            rDTO.setCategory(null);
            rList.add(rDTO);

            rDTO = null;
        }


        log.info(this.getClass().getName() + ".getTagCategory End!");

        return rList;
    }

    @Override
    public List<CategoryDTO> getCategoryAll() throws Exception {

        log.info(this.getClass().getName() + ".getCategoryAll Start!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<CategoryDTO> rList = new LinkedList<>();

        String colNm = "NLP_CATEGORY";

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        List<? extends Bson> pipeline = Arrays.asList(
                new Document()
                        .append("$graphLookup", new Document()
                                .append("from", colNm)
                                .append("startWith", "$parent")
                                .append("connectFromField", "parent")
                                .append("connectToField", "tag")
                                .append("as", "tree")
                                .append("depthField", "depth")
                        )
        );

        AggregateIterable<Document> rs = col.aggregate(pipeline).allowDiskUse(true);

        for (Document doc : rs) {
            log.info("doc : " + doc);

            String curentTag = CmmUtil.nvl(doc.getString("tag")); // 가져올 태그
            String parentTag = CmmUtil.nvl(doc.getString("parent")); // 부모 태그

            // 트리값 가져오기
            List<Document> tree = doc.getList("tree", Document.class, new LinkedList<>());

            // 트리 값 정렬
            // 나이(Integer) 내림차순
            Collections.sort(tree, (Comparator<Document>) (o1, o2) -> {
                Long depth1 = o1.getLong("depth");
                Long depth2 = o2.getLong("depth");
                return depth2.compareTo(depth1);
            });

            // 트리 결과를 저장할 객체
            List<String> treeList = new LinkedList<>();

            // 카테고리 순서대로 넣기
            for (Document tDoc : tree) {
                String treeTag = CmmUtil.nvl(tDoc.getString("tag"));

                log.info("treeTag : " + treeTag);

                treeList.add(treeTag);
            }

            treeList.add(curentTag); // 자기 자신도 넣기

            CategoryDTO rDTO = new CategoryDTO();
            rDTO.setCategory(curentTag);
            rDTO.setTree(treeList);

            rList.add(rDTO);

            treeList = null;
            rDTO = null;

        }

        rs = null;
        pipeline = null;
        col = null;

        log.info(this.getClass().getName() + ".getCategoryAll End!");

        return rList;
    }

    @Override
    public List<CategoryDTO> getTagCategoryAll() throws Exception {

        log.info(this.getClass().getName() + ".getTagCategoryAll Start!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<CategoryDTO> rList = new LinkedList<>();

        // 전체태그
        Set<String> tagSet = new HashSet<>();

        String colNm = "NLP_CLUSTER_DICTIONARY";

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        // 조회 결과 중 출력할 컬럼들(SQL의 SELECT절과 FROM절 가운데 컬럼들과 유사함)
        Document projection = new Document();
        projection.append("tag", "$tag");
        projection.append("tags", "$tags");
        projection.append("_id", 0);

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(new Document()).projection(projection);

        for (Document doc : rs) {
            tagSet.addAll((List) doc.getList("tags", String.class, new LinkedList<>()));

        }

        rs = null;

        for (String tag : tagSet) {

            List<CategoryDTO> cList = this.getTagCategory(new CategoryDTO(tag));



            rList.addAll(cList);

            cList = null;
        }


        log.info(this.getClass().getName() + ".getTagCategoryAll End!");

        return rList;

    }
}
