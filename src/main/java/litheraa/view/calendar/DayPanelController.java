package litheraa.view.calendar;

import litheraa.data.models.ProdTimeModel;
import litheraa.view.util.ComponentAdjuster;
import litheraa.view.util.DayPanelListener;
import litheraa.view.util.SizeStepListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class DayPanelController {
	private final ProdTimeModel prodTimeModel;
	private final Map<JPanel, List<Component>> dayPanels = new LinkedHashMap<>();
	private final Map<DayPanelListener.AspectRatio, List<GridBagConstraints>> constraintsMap = new HashMap<>();
	private final Map<SizeStepListener.Step, List<Font>> fonts = new HashMap<>();
	private final Map<SizeStepListener.Step, Icon[]> icons = new HashMap<>();

	private Dimension firstDimension;
	private Dimension secondDimension;
	private Dimension thirdimension;
	private Dimension fourthDimension;
	private Dimension fifthDimension;

	private final Container container;
	private boolean withProgressBar = false;

	private SizeStepListener.Step currentStep;

	public DayPanelController(ProdTimeModel prodTimeModel, Container container) {
		this.prodTimeModel = prodTimeModel;
		this.container = container;

		ClassLoader loader = DayPanelController.class.getClassLoader();
		ImageIcon goalIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("mission.png")));
		ImageIcon doneIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("magic-book.png")));
		ImageIcon blankIcon = new ImageIcon(Objects.requireNonNull(loader.getResource("writed-book.png")));

		icons.put(SizeStepListener.Step.FIRST, new Icon[]{
				new ImageIcon(goalIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH)),
				new ImageIcon(doneIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH)),
				new ImageIcon(blankIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH)),
		});
		icons.put(SizeStepListener.Step.SECOND, new Icon[]{
				new ImageIcon(goalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)),
				new ImageIcon(doneIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)),
				new ImageIcon(blankIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)),
		});
		icons.put(SizeStepListener.Step.THIRD, new Icon[]{
				new ImageIcon(goalIcon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)),
				new ImageIcon(doneIcon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)),
				new ImageIcon(blankIcon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)),
		});
		icons.put(SizeStepListener.Step.FOURTH, new Icon[]{
				new ImageIcon(goalIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH)),
				new ImageIcon(doneIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH)),
				new ImageIcon(blankIcon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH)),
		});
		icons.put(SizeStepListener.Step.FIFTH, new Icon[]{
				new ImageIcon(goalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)),
				new ImageIcon(doneIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)),
				new ImageIcon(blankIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)),
		});
	}

	public void buildDayPanels(boolean withDayLabel, boolean withLabelGroup, boolean withProgressBar) {
		boolean firstDayPanel = true;

		for (int i : prodTimeModel.getDates()) {
			JPanel dayPanel = new JPanel(new GridBagLayout());

			List<Component> components = new ArrayList<>(3);

			List<GridBagConstraints> horizontalC = new ArrayList<>(3);
			List<GridBagConstraints> squareC = new ArrayList<>(3);
			List<GridBagConstraints> verticalC = new ArrayList<>(3);

			Font dateFont = new Font("Aerial", Font.BOLD, 20);

			List<Font> firstStepFonts = new ArrayList<>(2);
			List<Font> secondStepFonts = new ArrayList<>(2);
			List<Font> thirdStepFonts = new ArrayList<>(2);
			List<Font> fourthStepFonts = new ArrayList<>(2);
			List<Font> fifthStepFonts = new ArrayList<>(2);

			if (withDayLabel) {
				DayLabel dayLabel = new DayLabel(this);
				dayLabel.setText(String.valueOf(i));

				components.add(dayLabel);
				dayPanel.add(dayLabel);

				horizontalC.add(getDayLabelC());
				squareC.add(getDayLabelC());
				verticalC.add(getDayLabelC());

				if (firstDayPanel) {
					firstStepFonts.add(dateFont);
					secondStepFonts.add(dateFont.deriveFont(30L));
					thirdStepFonts.add(dateFont.deriveFont(40L));
					fourthStepFonts.add(dateFont.deriveFont(50L));
					fifthStepFonts.add(dateFont.deriveFont(60L));

					firstDimension = new Dimension(23, 23);
					secondDimension = new Dimension(36, 36);
					thirdimension = new Dimension(50, 50);
					fourthDimension = new Dimension(63, 63);
					fifthDimension = new Dimension(66, 66);
				}
			}
			if (withLabelGroup) {
				LabelGroup labelGroup = new LabelGroup(this,
						prodTimeModel.getWritten(i),
						prodTimeModel.getDayGoal(i),
						prodTimeModel.getTimeId(i));
				components.add(labelGroup);
				dayPanel.add(labelGroup);

				horizontalC.add(getLabelGroupC(DayPanelListener.AspectRatio.HORIZONTAL));
				squareC.add(getLabelGroupC(DayPanelListener.AspectRatio.SQUARE));
				verticalC.add(getLabelGroupC(DayPanelListener.AspectRatio.VERTICAL));

				if (firstDayPanel) {
					labelGroup.addComponentListener(new SizeStepListener(this));
					firstStepFonts.add(dateFont.deriveFont(14L));
					secondStepFonts.add(dateFont.deriveFont(18L));
					thirdStepFonts.add(dateFont.deriveFont(22L));
					fourthStepFonts.add(dateFont.deriveFont(26L));
					fifthStepFonts.add(dateFont.deriveFont(30L));
				}
			}
			if (withProgressBar) {
				this.withProgressBar = true;

				JProgressBar progressBar = new JProgressBar();
				progressBar.setMaximum(prodTimeModel.getDayGoal(i));
				progressBar.setValue(prodTimeModel.getWritten(i));
				progressBar.setStringPainted(true);
				components.add(progressBar);
				dayPanel.add(progressBar);

				horizontalC.add(getProgressBarC(DayPanelListener.AspectRatio.HORIZONTAL));
				squareC.add(getProgressBarC(DayPanelListener.AspectRatio.SQUARE));
				verticalC.add(getProgressBarC(DayPanelListener.AspectRatio.VERTICAL));
			}
			if (firstDayPanel) {
				dayPanel.addComponentListener(new DayPanelListener(this));

				constraintsMap.put(DayPanelListener.AspectRatio.HORIZONTAL, horizontalC);
				constraintsMap.put(DayPanelListener.AspectRatio.SQUARE, squareC);
				constraintsMap.put(DayPanelListener.AspectRatio.VERTICAL, verticalC);

				fonts.put(SizeStepListener.Step.FIRST, firstStepFonts);
				fonts.put(SizeStepListener.Step.SECOND, secondStepFonts);
				fonts.put(SizeStepListener.Step.THIRD, thirdStepFonts);
				fonts.put(SizeStepListener.Step.FOURTH, fourthStepFonts);
				fonts.put(SizeStepListener.Step.FIFTH, fifthStepFonts);

				firstDayPanel = false;
			}
			dayPanels.put(dayPanel, components);
		}
	}

	public void addDayPanelsToContainer() {
		for (JPanel panel : dayPanels.keySet()) {
			container.add(panel);
		}
	}

	public void aspectRatioChanged(DayPanelListener.AspectRatio ratio) {
		dayPanels.forEach((key, value) -> {
			IntStream.range(0, value.size())
					.forEach(i -> key.add(value.get(i), constraintsMap.get(ratio).get(i)));
			if (withProgressBar) {
				((JProgressBar) value.getLast()).setOrientation(ratioToProgressBarOrientation(ratio));
			}
		});
	}

	public void sizeStepChanged(SizeStepListener.Step sizeStep) {
		currentStep = sizeStep;
		dayPanels.forEach((key, value) -> {
			int j = withProgressBar ? value.size() - 1 : value.size();
			{
				try (IntStream stream = IntStream.range(0, j)) {
					stream.forEach(i -> ((ComponentAdjuster) value.get(i)).adjust(fonts.get(sizeStep).get(i)));
				} catch (Exception ignored) {
				}
			}
		});
	}

	public Icon[] getIcon() {
		return icons.get(currentStep);
	}

	public Dimension getDimension() {
		return switch (currentStep) {
			case FIRST -> firstDimension;
			case SECOND -> secondDimension;
			case THIRD -> thirdimension;
			case FOURTH -> fourthDimension;
			case FIFTH -> fifthDimension;
		};
	}

	private int ratioToProgressBarOrientation(DayPanelListener.AspectRatio ratio) {
		if (ratio == DayPanelListener.AspectRatio.HORIZONTAL) {
			return SwingConstants.VERTICAL;
		} else return SwingConstants.HORIZONTAL;
	}

	public void setGoal(int goal, Long timeId) {

	}

	private GridBagConstraints getLabelGroupC(DayPanelListener.AspectRatio ratio) {
		GridBagConstraints horizontalAndSquare = new GridBagConstraints();
		horizontalAndSquare.fill = GridBagConstraints.BOTH;
		horizontalAndSquare.gridy = 0;
		horizontalAndSquare.gridx = 1;
		horizontalAndSquare.weightx = 0.5;
		horizontalAndSquare.weighty = 0.5;

		GridBagConstraints vertical = new GridBagConstraints();
		vertical.fill = GridBagConstraints.BOTH;
		vertical.gridy = 1;
		vertical.gridx = 0;
		vertical.weightx = 0.5;
		vertical.weighty = 0.5;

		return switch (ratio) {
			case HORIZONTAL, SQUARE -> horizontalAndSquare;
			case VERTICAL -> vertical;
		};
	}

	private GridBagConstraints getProgressBarC(DayPanelListener.AspectRatio ratio) {
		GridBagConstraints horizontal = new GridBagConstraints();
		horizontal.fill = GridBagConstraints.VERTICAL;
		horizontal.gridy = 0;
		horizontal.gridx = 2;
		horizontal.weightx = 0;
		horizontal.weighty = 1.0;
		horizontal.gridwidth = 1;

		GridBagConstraints square = new GridBagConstraints();
		square.fill = GridBagConstraints.HORIZONTAL;
		square.gridy = 1;
		square.gridx = 0;
		square.weightx = 1.0;
		square.weighty = 0;
		square.gridwidth = 2;

		GridBagConstraints vertical = new GridBagConstraints();
		vertical.fill = GridBagConstraints.HORIZONTAL;
		vertical.gridy = 2;
		vertical.gridx = 0;
		vertical.weightx = 1.0;
		vertical.weighty = 0;
		vertical.gridwidth = 1;

		return switch (ratio) {
			case HORIZONTAL -> horizontal;
			case SQUARE -> square;
			case VERTICAL -> vertical;
		};
	}

	private @NotNull GridBagConstraints getDayLabelC() {
		GridBagConstraints dayLabelC = new GridBagConstraints();
		dayLabelC.fill = GridBagConstraints.BOTH;
		dayLabelC.gridy = 0;
		dayLabelC.gridx = 0;
		return dayLabelC;
	}
}
