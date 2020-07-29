package payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;*/

@SpringBootApplication
/*@ComponentScan({"conn.DatasourceFactory"})
@EntityScan("conn.DatasourceFactory")
@EnableJpaRepositories("conn.DatasourceFactory")*/
public class PayrollApplication {
	
	public static void main(String... args) {
		SpringApplication.run(PayrollApplication.class,args);
	}
}
