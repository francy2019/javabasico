package com.mycompany.socialnetwork.service;

import com.mycompany.socialnetwork.dto.PageRequest;
import com.mycompany.socialnetwork.dto.PageResponse;
import com.mycompany.socialnetwork.dto.PostRequest;
import com.mycompany.socialnetwork.dto.PostResponse;

public interface PageService {

    PageResponse create(PageRequest page);
    PageResponse readByTitle(String title);
    PageResponse update(PageRequest page, String title);
    void delete(String title);

    PageResponse createPost(PostRequest post, String title);
    void deletePost(Long idPost,String title);

}
