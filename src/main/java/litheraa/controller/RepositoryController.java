package litheraa.controller;

import litheraa.data.TextFinder;
import litheraa.data.entities.Prod;
import litheraa.data.entities.Time;
import litheraa.data.entities.Text;
import litheraa.data.repository.ProdRepository;
import litheraa.data.repository.TimeRepository;
import litheraa.data.repository.TextRepository;
import litheraa.data.suppliers.ProdSupplier;
import litheraa.data.suppliers.TimeSupplier;
import litheraa.util.readers.ReaderFactory;
import litheraa.util.readers.ReaderInterface;
import litheraa.data.suppliers.TextSupplier;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class RepositoryController {
	private static TextRepository textRepository;
	private static ProdRepository prodRepository;
	private static TimeRepository timeRepository;
	@Getter
	private static final Set<Integer> uniqueYears = new HashSet<>();

	@Autowired
	public RepositoryController(TextRepository textRepository, ProdRepository prodRepository, TimeRepository timeRepository) {
		RepositoryController.textRepository = textRepository;
		RepositoryController.prodRepository = prodRepository;
		RepositoryController.timeRepository = timeRepository;
	}

	private static void collect(List<Path> files, LocalDate from) {
		try {
		for (Path path : TextFinder.findProd(files)) {
			ReaderInterface reader = ReaderFactory.createReader(path);
			LocalDate date = reader.getLastModifiedDate();

			if (from.minusDays(1).isBefore(date)) {
				Text text = Optional.ofNullable(textRepository.findTopByPath(path.toString())).
						orElseGet(new TextSupplier(reader));
				if (text.getId() == null) {
					textRepository.save(text);
				}
				Time time = Optional.ofNullable(timeRepository.findByModified(date)).
						orElseGet(new TimeSupplier(reader));
				Prod prod = Optional.ofNullable(text.getProd()).
						orElseGet(new ProdSupplier(text, time));
				if (!Objects.equals(prod.getId().getTimeId(), time.getId())) {
					prod.setWritten(0);
				}
				if (prod.setChars(reader.getCharacters())) {
					time.addWritten(prod.getWritten());
					timeRepository.save(time);
					prod.getId().setTimeId(time.getId());
				}
				prod.setName(reader.getProdName(prod));
				prodRepository.save(prod);
				uniqueYears.add(time.getModified().getYear());
			}
		}
	} catch (Exception e) {
			log.error("e: {}", String.valueOf(e));
		}
	}

	public static void collectNewData(List<Path> files) {
		collect(files, LocalDate.now());
	}

	public static void collectData(List<Path> files) {
		collect(files, LocalDate.of(1970, 1, 1));
	}

	public static Pair<List<Time>, List<Text>> getDataByPeriod(LocalDate from, LocalDate to) {
		return new Pair<>(timeRepository.findByModifiedBetween(from, to),
				IteratorUtils.toList(textRepository.findAll().iterator()));
	}
//TODO не должно работать из-за нулевых ID
	public static void setGoal(int goal, Long... timeId) {
		List<Long> list = new ArrayList<>(Arrays.asList(timeId));
		List<Time> times = new ArrayList<>();
		for (Time time : timeRepository.findAllById(list)) {
			time.setGoal(goal);
			times.add(time);
		}
		timeRepository.saveAll(times);
	}

	public static void autoRun(boolean isAutoRun) {
		String[] s = new String[1];
		if (isAutoRun) {
			s[0] = "cmd /C reg add HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run /v " +
					"MySquperApp /t REG_SZ /d \"%PROGRAMFILES%\\MySquperApp\\MySquperApp.lnk\" /f";
		} else {
			s[0] = "cmd /C reg delete HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run " +
					"/v MySquperApp /f\r\n";
		}
		try {
			Runtime.getRuntime().exec(s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
