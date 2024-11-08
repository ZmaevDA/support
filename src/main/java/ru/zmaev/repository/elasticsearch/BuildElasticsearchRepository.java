package ru.zmaev.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.document.BuildDocument;
@Repository
public interface BuildElasticsearchRepository extends ElasticsearchRepository<BuildDocument, Long> {

    @Query("{\"bool\": {\"should\": [{\"match_phrase_prefix\": {\"name\": \"?0\"}}, {\"match_phrase_prefix\": {\"description\": \"?0\"}}], \"minimum_should_match\": 1}}")
    Page<BuildDocument> findAllSearch(String search, Pageable pageable);
}
