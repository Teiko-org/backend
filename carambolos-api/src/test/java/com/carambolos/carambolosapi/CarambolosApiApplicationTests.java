package com.carambolos.carambolosapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Contexto completo depende de recursos externos (DB/Rabbit). Desabilitado no CI.")
@SpringBootTest
class CarambolosApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
