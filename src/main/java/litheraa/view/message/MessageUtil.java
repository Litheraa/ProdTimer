package litheraa.view.message;

import litheraa.util.ProjectFolderUtil;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public final class MessageUtil extends ProjectFolderUtil {
	private static final String SEPARATOR = "=";
	private static final String ABOUT;
	private static final File FILE;
	private static final List<String> TIPS = new ArrayList<>();
	private static final Map<Integer, Pair<String, ImageIcon>> MESSAGES = new HashMap<>();

	static {
		FILE = new File(String.valueOf(ProjectFolderUtil.getMessageFile()));
	}

	static {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE));
			Map<Boolean, List<String>> map = bufferedReader.lines().
					collect(Collectors.partitioningBy(line -> line.startsWith("tip")));
			TIPS.addAll(map.get(true));
			for (int i = 0; i < TIPS.size(); i++) {
				ImageIcon icon;
				try {
					icon = new ImageIcon(Objects.requireNonNull(
							MessageUtil.class.getClassLoader().getResource("tipIcon" + i + ".png")));
					} catch (NullPointerException e) {
					icon = null;
				}
				String tip = TIPS.get(i);
				tip = tip.substring(tip.indexOf(SEPARATOR) + 1);
				Pair<String, ImageIcon> pair = new Pair<>(tip, icon);
				MESSAGES.put(i, pair);
			}
			ABOUT = String.join("\n", map.get(false));
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	static int getTipCount() {
		return TIPS.size();
	}

	static Pair<String, ImageIcon> getTip(int tipNo) {
		return MESSAGES.get(tipNo);
	}

	static String getAbout() {
		return ABOUT.replaceFirst("about" + SEPARATOR, "");
	}
}
