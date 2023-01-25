/* @File("org.some.demo.data.repository.PersonRepository") */
/*
    @Name("findPersons")
    @Param(name="afterId", type=int)
    @Returns("org.some.demo.model.PersonDto")
*/
select * from Person p
where p.id > :afterId
--