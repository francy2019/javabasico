package com.mycompany.socialnetwork;

import com.mycompany.socialnetwork.dto.PageRequest;
import com.mycompany.socialnetwork.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SocialnetworkApplication implements  CommandLineRunner{

	@Autowired
	private PageService pageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {

		SpringApplication.run(SocialnetworkApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//var password= "secret";// email y password
		//var passwordEncoded = this.passwordEncoder.encode(password);
		//System.out.println(passwordEncoded);
	}
/* comentariamos porque ya no samos el CommanRuner
	@Override
	public void run(String... args) throws Exception {
		var req = PageRequest.builder()
				//.userId(4L)  // se comentareo para probar el update
				.title("Prueba Domingo")
				.build();


		//Agregar Pagina
		//var res = this.pageService.create(req);

		//Consultar Pagina por Titulo
		//var res = this.pageService.readByTitle("User2 Page");

		//Actualizar Pagina, solo actualiza el titulo, no los Post
		//var res = this.pageService.update(req,"User1 Page");

		//Borrar Pagina
		//this.pageService.delete("User2 Page");
		//System.out.println(res);
	}
*/

}
