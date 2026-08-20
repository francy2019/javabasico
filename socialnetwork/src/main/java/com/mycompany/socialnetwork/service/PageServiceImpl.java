package com.mycompany.socialnetwork.service;

import com.mycompany.socialnetwork.dto.PageRequest;
import com.mycompany.socialnetwork.dto.PageResponse;
import com.mycompany.socialnetwork.dto.PostRequest;
import com.mycompany.socialnetwork.dto.PostResponse;
import com.mycompany.socialnetwork.entities.PageEntity;
import com.mycompany.socialnetwork.entities.PostEntity;
import com.mycompany.socialnetwork.exceptions.TitleNoValidException;
import com.mycompany.socialnetwork.repository.PageRepository;
import com.mycompany.socialnetwork.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PageServiceImpl implements PageService {

    private  final PageRepository pageRepository;
    private  final UserRepository userRepository;

    @Override
    public PageResponse create(PageRequest page) {

        this.validTitle(page.getTitle());
        final var entity= new PageEntity(); //crea el objeto Entity para persstir en BD

        //serializacion para convertir el PageRequest(DTO_Request) en Entity, mapeo Origen, Destino
        BeanUtils.copyProperties(page, entity);

        //para obtener el objeto del usuario
        final  var user = this.userRepository.findById(page.getUserId()).orElseThrow();

        entity.setDateCreation(LocalDateTime.now()); // setea la fecha de creacion, osea fecha actual
        entity.setUser(user); // creala relacion entre usuario y pagina
        entity.setPosts(new ArrayList<>()); // la pagina se crea sin Post al inicio, por eso se envia el array vacio

        // Guarda en BD, que retorna la pagina creada
        var pageCreated = this.pageRepository.save(entity);

        final var response = new PageResponse();
        //serializacion para convertir la respuesta de la Entity(BD) al DTO_Response
        BeanUtils.copyProperties(pageCreated, response);

        return response;
    }

    @Override
    public PageResponse readByTitle(String title) {
        //Busca por tituloa, en caso de que llegue vacio envia la xcepcion del titulo o encontrado
        final  var entityResponse = this.pageRepository.findByTitle(title)
                .orElseThrow(()->new IllegalArgumentException("Title not found"));


        final  var response = new PageResponse();
        BeanUtils.copyProperties(entityResponse, response);

        //Obtiene los post apartir del os retornados de la bd en la respuesa de la consulta de la Pagina, de la relacion Pagina-Post
        final List<PostResponse> postResponses = entityResponse.getPosts().stream() //Convierte a Stream
                .map(postEnt-> PostResponse  //Recorre la respuesta del Entity para maperlo a DTO PostResponse
                        .builder()                      // crea el objeto postResponse y comienza a agregar los atributos
                        .img(postEnt.getImg())
                        .content(postEnt.getContent())
                        .dateCreation(postEnt.getDateCreation())
                        .build()
                    ).toList(); // luego lo convierte a Lista, el recorrido de cada objeto
        response.setPost(postResponses);
        return response;
    }

    @Override
    public PageResponse update(PageRequest page, String title) {
        //Busca por titulo, en caso de que llegue vacio envia la xcepcion del titulo o encontrado
        final var entityFromDB = this.pageRepository.findByTitle(title)
                .orElseThrow(()->new IllegalArgumentException("Title not found"));

        // actualizamos a nivel de objeto, es decir el que retorno la Bd, asignandole los que nos llegan como parametro de la funcion, osea los el Req
        entityFromDB.setTitle(page.getTitle());

        // Guarda en BD, que retorna la pagina creada
        var pageCreated = this.pageRepository.save(entityFromDB); // en este caso el save funciona como update o create, udate porque ya existe el ID

        final var response = new PageResponse();
        //serializacion para convertir la respuesta de la Entity(BD) al DTO_Response
        BeanUtils.copyProperties(pageCreated, response);
        return response;
    }

    @Override
    public void delete(String title) {
        // ejemplo de borrar  por Id, en este caso el Id seria 1, se le coloca la L por el long
       // this.pageRepository.deleteById(1L);
        if(this.pageRepository.existsByTitle(title)){
            log.info("Deleting Page");
            this.pageRepository.deleteByTitle(title);
        }else{
            log.error("Error to Delete");
            throw  new IllegalArgumentException("Title does not exist");
        }
    }

    @Override
    public PageResponse createPost(PostRequest post, String title) {

        final  var pageToUpdate = this.pageRepository.findByTitle(title)
                .orElseThrow(()->new IllegalArgumentException("Title not found"));

        final var postEntity = new PostEntity(); //Post a agregar en la BD

        BeanUtils.copyProperties(post, postEntity);
        postEntity.setDateCreation(LocalDateTime.now());
        pageToUpdate.addPost(postEntity); // se actualiza Post en la pagina

        final var responseEntity = this.pageRepository.save(pageToUpdate); // Envia a Actualizar la pagina

        final var response = new PageResponse();
        BeanUtils.copyProperties(responseEntity, response);

        final List<PostResponse> postResponses = responseEntity.getPosts().stream() //Convierte a Stream
                .map(postEnt-> PostResponse  //Recorre la respuesta del Entity para maperlo a DTO PostResponse
                        .builder()                      // crea el objeto postResponse y comienza a agregar los atributos
                        .img(postEnt.getImg())
                        .content(postEnt.getContent())
                        .dateCreation(postEnt.getDateCreation())
                        .build()
                ).toList(); // luego lo convierte a Lista, el recorrido de cada objeto
        response.setPost(postResponses);
        return response;
    }

    @Override
    public void deletePost(Long idPost, String title) {
        final  var pageToUpdate = this.pageRepository.findByTitle(title)
                .orElseThrow(()->new IllegalArgumentException("Title not found"));

        final var postToDelete =pageToUpdate.getPosts()
                .stream()
                .filter(post -> post.getId().equals(idPost))
                .findFirst()
                .orElseThrow(() ->new IllegalArgumentException("Post ID no found"));;

        pageToUpdate.removePost(postToDelete);
    }

    private void  validTitle(String title){
        if (title.contains("12345")){
            throw  new TitleNoValidException("Titulo cant contain bad words");
        }
    }
}
