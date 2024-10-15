package com.julio.curso.springboot.jpa.springboot_jpa;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.julio.curso.springboot.jpa.springboot_jpa.entities.Person;
import com.julio.curso.springboot.jpa.springboot_jpa.repositories.PersonRepository;

@SpringBootApplication
public class SpringbootJpaApplication implements CommandLineRunner{

	@Autowired
	private PersonRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//list();
		//findOne();
		//create();
		//delete2();
		personalizedQueries();
	}

	@Transactional(readOnly = true)
	public void personalizedQueries(){

		Scanner sc = new Scanner(System.in);

		System.out.println("=============== consulta solo el nombre por id =================");
		System.out.println("Ingrese el id para el nombre:");
		Long id = sc.nextLong();

		sc.close();

		System.out.println("=========== Mostrando solo el nombre ============");
		String name = repository.getNameById(id);
		System.out.println(name);

		System.out.println("=========== Mostrando solo el id ============");
		Long idDb = repository.getIdById(id);
		System.out.println(idDb);

		System.out.println("=========== Mostrando el nombre completo con concat ============");
		String fullName = repository.getFullNameById(id);
		System.out.println(fullName);

		System.out.println("=============== consulta de todos los datos por id =================");
		Optional<Object> optionalReg =  repository.obtenerPersonDataById(id);
		if (optionalReg.isPresent()) {
			Object[] personReg= (Object[]) optionalReg.orElseThrow();
			System.out.println("id=" + personReg[0] + ", nombre=" + personReg[1] + ", apellido=" + personReg[2] + ", lenguaje de programacion=" + personReg[3]);
		}
		

		System.out.println("=============== consulta de todos los datos por id lista =================");
		List<Object[]> regs = repository.obtenerPersonDataList();
		regs.forEach(reg -> System.out.println("id=" + reg[0] + ", nombre=" + reg[1] + ", apellido=" + reg[2] + ", lenguaje de programacion=" + reg[3]));
	}

	@Transactional
	public void create() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el nombre:");
		String name = sc.next();
		System.out.println("Ingrese el apellido:");
		String lastname = sc.next();
		System.out.println("Ingrese el lenguaje de programacion:");
		String pl = sc.next();
		sc.close();
		Person p = new Person(null,name,lastname,pl);

		Person pNew = repository.save(p);

		//System.out.println(pNew);

		repository.findById(pNew.getId()).ifPresent(System.out::println);
	}

	@Transactional
	public void delete() {

		repository.findAll().forEach(System.out::println);
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona a eliminar:");
		Long id = sc.nextLong();
		sc.close();

		repository.deleteById(id);
		
		repository.findAll().forEach(System.out::println);

	}

	@Transactional
	public void delete2() {

		repository.findAll().forEach(System.out::println);
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona a eliminar:");
		Long id = sc.nextLong();
		sc.close();

		Optional<Person> optionalPerson = repository.findById(id);

		optionalPerson.ifPresentOrElse(repository::delete,
		() -> System.out.println("Lo sentimos no existe una persona con ese id"));
		
		repository.findAll().forEach(System.out::println);

	}

	@Transactional
	public void update() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese el id de la persona a actualizar:");
		Long id = sc.nextLong();

		Optional<Person> optionalPerson = repository.findById(id);

		//p.ifPresent(person -> {
		if (optionalPerson.isPresent()) {
			Person personDB = optionalPerson.orElseThrow();

			System.out.println("Ingrese el lenguaje de programacion:");
			String programmingLanguage = sc.next();
			personDB.setProgrammingLanguage(programmingLanguage);
			Person personUpdate = repository.save(personDB);
			System.out.println(personUpdate);
		} else {
			System.out.println("No existe una persona con ese id");
		}
			
		//});
		sc.close();
	}

	@Transactional(readOnly = true)
	public void findOne(){
		/*
		Person person = null;
		Optional<Person> optionalPerson = repository.findById(1L);
		if (optionalPerson.isPresent()) {
			person = optionalPerson.get();
		} 
		System.out.println(person);
		*/
		//repository.findById(1L).ifPresent(System.out::println);
		repository.findByNameContaining("Pe").ifPresent(System.out::println);
	}

	@Transactional(readOnly = true)
	public void list(){
		//List<Person> persons = (List<Person>) repository.findAll();
		//List<Person> persons = (List<Person>) repository.buscarByProgrammingLanguage("Java", "Andres");
		List<Person> persons = (List<Person>) repository.findByProgrammingLanguageAndName("Java", "Andres");

		persons.stream().forEach(person -> System.out.println(person));
		
		List<Object[]> personsData = repository.obtenerPersonData();

		personsData.stream().forEach(person -> {
			System.out.println(person[0] + " es experto en " + person[1]);
		});
	}

}
