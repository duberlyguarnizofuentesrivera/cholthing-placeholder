# Clothing Placeholder
By Duberly Guarnizo

---------------------------

A fast image placeholder service oriented to clothing store design, using creative commons images.
You han request an image specifying some of the parameters on the next list. A request with no parameters will return the image with a resolution of 1080*1920 (vertical aspect ratio):
- w = <b>Width</b>. Defaults to 1080px.
- h = <b>Height</b> (if this option is not present, but "w" is, a w*(16*w/9) image size will be returned, this is the image scaled with 9-16 aspect ratio). Defaults to 1920.
- r = Aspect <b>ratio</b>. If "w" or "h" are present, will return the image with the present dimension with the other dimension resized to match the "r" aspect ratio. Must be passed with the form "x-y", i.e.: <i>host.com/?r=2-3</i>. Default value is 9-16.
- p = Integer number of pixels of white <b>padding</b> around the image. Defaults to 0.
- c = Background <b>color</b> of the image. Values are passed as HEX strings, i.e.: <i>host.com/?c=#4522ff</i>. Default is transparent.

### Reference Documentation

Used Libraries and tools


- Docker Compose, to create containers.
- Spring Boot 3.1, for the backend.
- Spring Data JPA, for the ORM.
- Spring Security, for the admin panel security.
- PostgreSQL for the database, but the system is database agnostic.
- Spring Boot Actuator, to generate stats of usage.
- Swagger, to generate endpoint information for API usage.
- TestContainers and JUnit, for integration and unit testing. 
- Thymeleaf, for the (very simple) frontend.
- Bootstrap, to style the frontend.

### Guides
Compilation target is Java 17. Dependency management is Gradle.
