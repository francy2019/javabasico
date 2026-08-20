package com.mycompany.socialnetwork.controllers;

import com.mycompany.socialnetwork.dto.PageRequest;
import com.mycompany.socialnetwork.dto.PageResponse;
import com.mycompany.socialnetwork.dto.PostRequest;
import com.mycompany.socialnetwork.service.PageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@RestController  // Expone servicios Rest
@RequestMapping(path = "page") // Indica el EndPoint de este controles
@AllArgsConstructor
public class PageController {

    private  final PageService pageService;

    @GetMapping(path = "{title}")  // metodo HTTP para obtener datos
    public ResponseEntity<PageResponse> getPage(@PathVariable String title){
        return  ResponseEntity.ok(pageService.readByTitle(title));
    }

    @PostMapping // metodo HTTP para eniar datos
    public ResponseEntity<?> postPage(@RequestBody PageRequest request){

        request.setTitle(this.quitarEspacios(request.getTitle()));

        final var uri = this.pageService.create(request).getTitle();
        return  ResponseEntity.created(URI.create(uri)).build();
    }

    @PutMapping(path = "{title}") // metodo HTTP para actualizar datos
    public ResponseEntity<PageResponse> updatePage(@PathVariable String title,@RequestBody PageRequest request){
        return ResponseEntity.ok(this.pageService.update(request,title));
    }

    @DeleteMapping(path = "{title}") // metodo HTTP para eliminar datos
    public ResponseEntity<Void> deletePage(@PathVariable String title){
        this.pageService.delete(title);
        return ResponseEntity.noContent().build();
    }

    public String quitarEspacios(String title){
        if (title.contains(" ")){
            return title.replace(" ", "-");
        }else {
            return title;
        }
    }

    @PostMapping(path = "{title}/post") // metodo HTTP para eniar datos
    public ResponseEntity<PageResponse> postPage(@RequestBody PostRequest request, @PathVariable String title){

        return ResponseEntity.ok(this.pageService.createPost(request,title));

    }

    @DeleteMapping(path = "{title}/post/{idPost}") // metodo HTTP para eliminar datos
    public ResponseEntity<Void> deletePage(@PathVariable String title, @PathVariable Long idPost){
        this.pageService.deletePost(idPost,title);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "img/upload") // metodo HTTP para eniar datos
    public ResponseEntity<String> upload(@RequestParam(value="file")MultipartFile file){
        try {
            final var  pathUrl = "C:/Users/ADMIN/Documents/PoyectoSpringBoot/socialnetwork/socialnetwork/src/main/resources/static/img";
            final var fullName = pathUrl + "/" + file.getOriginalFilename();
            final var destination = new File(fullName);

            file.transferTo(destination);
            return ResponseEntity.ok("Upload success on: " + fullName);

        }catch (IOException e){
            return null;
        }

    }
    

}
