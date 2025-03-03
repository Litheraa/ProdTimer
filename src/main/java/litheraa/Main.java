package litheraa;

import litheraa.controller.ProdTimerController;
import litheraa.controller.RepositoryController;
import litheraa.data.repository.ProdRepository;
import litheraa.data.repository.TextRepository;
import litheraa.data.repository.TimeRepository;
import litheraa.util.SpringContext;
import litheraa.util.SpringContextReaders;
import litheraa.util.readers.ReaderFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
//		new AnnotationConfigApplicationContext(SpringContextReaders.class).getBean(ReaderFactory.class);
//		new ProdTimerController();
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
		builder.headless(false);
		builder.run(args);
//		new AnnotationConfigApplicationContext(SpringContext.class).getBean(TextRepository.class);
//		new AnnotationConfigApplicationContext(SpringContext.class).getBean(TimeRepository.class);
//		new AnnotationConfigApplicationContext(SpringContext.class).getBean(ProdRepository.class);
//		List<Path> paths = new ArrayList<>();
//		File file = new File("C:\\Users\\Lithera\\Saved Games\\Desktop\\prod");
//		paths.add(file.toPath());
//		RepositoryController.collectData(paths);

//		public Double getAuthorPages() {
//			BigDecimal bD = BigDecimal.valueOf(getCharacters() / 40000.0);
//			return bD.setScale(3, RoundingMode.HALF_UP).doubleValue();
//		}

	}
}
