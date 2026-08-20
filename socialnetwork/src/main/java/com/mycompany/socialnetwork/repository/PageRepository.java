package com.mycompany.socialnetwork.repository;

import com.mycompany.socialnetwork.entities.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface PageRepository extends JpaRepository<PageEntity, Long> {

    // Buscar por titulo
    /*Hay otra opcion que es JPQL que seria en el @Query enviar el SQl pero asociado a la Entidad no a la tabla directamente
         @Query("from PageEntity where title=:title")
         Optional<PageEntity> findByTitle(String title);
    */

    //JPAQueryMethod: es como si dijeramos SELECT * FROM Page WHERE title = title
    Optional<PageEntity> findByTitle(String title);


    //Borrar por Titulo
    @Modifying // indica que se esta modificando algo en la Bd, que se esta escribiendo
    @Query("DELETE FROM PageEntity WHERE title=:title")  //Validar si existe por Titulo, en JPA trae un validar pero por Id
    void deleteByTitle(String title);

    boolean existsByTitle(String title);

}
