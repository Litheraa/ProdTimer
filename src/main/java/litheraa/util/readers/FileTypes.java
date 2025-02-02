package litheraa.util.readers;

public record FileTypes(String docx, String odt, String doc) {

	private static final String STAR = "*.";

	public FileTypes() {
		this("docx", "odt", "doc");
	}

	public String getFullNames() {
		return "Word document (" + STAR + docx + ") or (" + STAR + doc + "), Open document text (" + STAR + odt + ")";
	}

	/// NB! For JFileChooser file types set one by one due to bug
	public String[] getWildCards() {
		return new String[]{STAR + docx, STAR + odt, STAR + doc};
	}
}
