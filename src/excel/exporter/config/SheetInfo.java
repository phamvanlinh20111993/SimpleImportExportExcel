package excel.exporter.config;

public class SheetInfo {
	
	private boolean isFitToPage;
	
	private String name;
	
	private boolean isDisplayGrid;  
	
	private FontInfo font;

	public boolean isFitToPage() {
		return isFitToPage;
	}

	public void setFitToPage(boolean isFitToPage) {
		this.isFitToPage = isFitToPage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDisplayGrid() {
		return isDisplayGrid;
	}

	public void setDisplayGrid(boolean isDisplayGrid) {
		this.isDisplayGrid = isDisplayGrid;
	}

	public FontInfo getFont() {
		return font;
	}

	public void setFont(FontInfo font) {
		this.font = font;
	}
	
	
}
