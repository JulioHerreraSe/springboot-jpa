package com.julio.curso.springboot.jpa.springboot_jpa;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.julio.curso.springboot.jpa.springboot_jpa.dto.PersonDto;
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
		create();
		//update();
		//delete2();
		//personalizedQueries();
		//personalizedQueries2();
		//personalizedQueriesDistinct();
		//personalizedQueriesConcatUpperAndLowerCase();
		//personalizedQueriesBetween();
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

	@Transactional(readOnly = true)
	public void personalizedQueries2(){

		System.out.println("=============== consulta por objeto persona y lenguaje de programacion =================");
		List<Object[]> personsRegs = repository.findAllMixPerson();

		personsRegs.forEach(reg -> {
			System.out.println("programmingLanguage=" + reg[1] + ", person=" + reg[0]);
		});

		System.out.println("Consulta que puebla y devuelve objeto entity de una instancia personalizada");

		List<Person> persons = repository.findAllObjectPersonPersonalized();
		persons.forEach(System.out::println);

		System.out.println("Consulta que puebla y devuelve objeto dto de una clase dto personalizada");

		List<PersonDto> personsDto = repository.findAllObjectPersonDto();
		personsDto.forEach(System.out::println);
	}

	@Transactional(readOnly = true)
	public void personalizedQueriesConcatUpperAndLowerCase(){

		System.out.println("Consultas con nombres y apellidos de personas");
		List<String> names = repository.findAllFullNameConcat();
		names.forEach(System.out::println);

		System.out.println("Consultas con nombres y apellidos mayuscula de personas");
		names = repository.findAllFullNameConcatUpper();
		names.forEach(System.out::println);

		System.out.println("Consultas con nombres y apellidos minusculas de personas");
		names = repository.findAllFullNameConcatLower();
		names.forEach(System.out::println);
	}

	@Transactional(readOnly = true)
	public void personalizedQueriesDistinct(){
		System.out.println("Consultas con nombres de personas");
		List<String> names = repository.findAllNames();
		names.forEach(System.out::println);

		System.out.println("Consultas con nombres de personas unicos");
		List<String> namesDistinct = repository.findAllNamesDistinct();
		namesDistinct.forEach(System.out::println);

		System.out.println("Consultas con lenguajes de programacion unicos");
		List<String> languages = repository.findAllPrammingLanguageDistinct();
		languages.forEach(System.out::println);

		System.out.println("numero de lenguajes de programacion unicos");
		Long totalLenguage = repository.findAllPrammingLanguageDistinctCount();
		System.out.println("Numero de lenguajes distintos" + totalLenguage);
	}

	@Transactional(readOnly = true)
	public void personalizedQueriesBetween(){
		List<Person> persons = repository.findAllBetweenId(2L,4L);
		persons.forEach(System.out::println);

		persons = repository.findAllBetweenName("J","Q");
		persons.forEach(System.out::println);

		persons = repository.findByIdBetween(3L,6L);
		persons.forEach(System.out::println);

		persons = repository.findByNameBetween("A","L");
		persons.forEach(System.out::println);

		persons = repository.findByIdBetweenOrderByIdDesc(2L,6L);
		persons.forEach(System.out::println);

		persons = repository.findByNameBetweenOrderByNameDescLastnameDesc("J","Q");
		persons.forEach(System.out::println);

		persons = repository.getAllOrdered();
		persons.forEach(System.out::println);

		Long totalPerson = repository.getTotalPerson();
		System.out.println(totalPerson);

		Long minId = repository.getMinId();
		System.out.println(minId);

		Long maxId = repository.getMaxId();
		System.out.println(maxId);

		List<Object[]> regs = repository.getPersonNameLength();
		regs.forEach(reg -> {
			String name = (String) reg[0];
			Integer lenght = (Integer) reg[1];
			System.out.println("name= " + name + " lenght= " + lenght);
		});

		Object[] resumReg = (Object[]) repository.getResumaAggregationFunction();
		System.out.println("min=" + resumReg[0] + "max=" + resumReg[1] + "sum=" + resumReg[2] + "avg=" + resumReg[3] + "count=" + resumReg[4]);

		List<Object[]> registers = repository.getShorterName();
		registers.forEach(reg -> {
			System.out.println("Name=" + reg[0] + "length=" + reg[1]);
		});

		Optional<Person> lastPerson = repository.getLastRegistratione();
		lastPerson.ifPresent(System.out::println);

		List<Person> personsList = repository.getPersonsByIds(Arrays.asList(1L, 2L, 5L, 6L));
		personsList.forEach(System.out::println);
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
