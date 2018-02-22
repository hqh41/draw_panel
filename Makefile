# Executables
OSTYPE = $(shell uname -s)
JAVAC = javac
JAVA = java
A2PS = a2ps-utf8
GHOSTVIEW = gv
DOCP = javadoc
ARCH = zip
PS2PDF = ps2pdf -sPAPERSIZE=a4
DATE = $(shell date +%Y-%m-%d-%H-%M-%S)
# Options de compilation
#CFLAGS = -verbose
CFLAGS =
CLASSPATH=.

JAVAOPTIONS = --verbose

PROJECT=TP_FiguresEditor
# nom du fichier d'impression
OUTPUT = $(PROJECT)
# nom du répertoire ou se situera la documentation
DOC = doc
# lien vers la doc en ligne du JDK
WEBLINK = "http://docs.oracle.com/javase/8/docs/api/"
# lien vers la doc locale du JDK
LOCALLINK = "file:///Users/davidroussel/Documents/docs/java/api/"
# nom de l'archive
ARCHIVE = $(PROJECT)
# format de l'archive pour la sauvegarde
ARCHFMT = zip
# Répertoire source
SRC = src
# Répertoire bin
BIN = bin
# Répertoire Listings
LISTDIR = listings
# Répertoire Archives
ARCHDIR = archives
# Répertoire Figures
FIGDIR = graphics
# noms des fichiers sources
MAIN = Editor ShapesDemo2D
SOURCES = $(foreach name, $(MAIN), $(SRC)/$(name).java) \
$(SRC)/figures/package-info.java \
$(SRC)/figures/Figure.java \
$(SRC)/figures/NGon.java \
$(SRC)/figures/Polygon.java \
$(SRC)/figures/Rectangle.java \
$(SRC)/figures/RoundedRectangle.java \
$(SRC)/figures/Circle.java \
$(SRC)/figures/Drawing.java \
$(SRC)/figures/Ellipse.java \
$(SRC)/figures/enums/package-info.java \
$(SRC)/figures/enums/FigureType.java \
$(SRC)/figures/enums/LineType.java \
$(SRC)/figures/enums/PaintToType.java \
$(SRC)/figures/listeners/package-info.java \
$(SRC)/figures/listeners/AbstractFigureListener.java \
$(SRC)/figures/listeners/SelectionFigureListener.java \
$(SRC)/figures/listeners/creation/package-info.java \
$(SRC)/figures/listeners/creation/AbstractCreationListener.java \
$(SRC)/figures/listeners/creation/NGonCreationListener.java \
$(SRC)/figures/listeners/creation/PolygonCreationListener.java \
$(SRC)/figures/listeners/creation/RectangularShapeCreationListener.java \
$(SRC)/figures/listeners/creation/RoundedRectangleCreationListener.java \
$(SRC)/figures/listeners/transform/package-info.java \
$(SRC)/figures/listeners/transform/AbstractTransformShapeListener.java \
$(SRC)/figures/listeners/transform/MoveShapeListener.java \
$(SRC)/figures/listeners/transform/RotateShapeListener.java \
$(SRC)/figures/listeners/transform/ScaleShapeListener.java \
$(SRC)/figures/treemodels/package-info.java \
$(SRC)/figures/treemodels/AbstractFigureTreeModel.java \
$(SRC)/figures/treemodels/FigureTreeModel.java \
$(SRC)/figures/treemodels/AbstractTypedFigureTreeModel.java \
$(SRC)/figures/treemodels/FigureTypeTreeModel.java \
$(SRC)/figures/treemodels/FillColorTreeModel.java \
$(SRC)/figures/treemodels/EdgeColorTreeModel.java \
$(SRC)/figures/treemodels/LineTypeTreeModel.java \
$(SRC)/filters/package-info.java \
$(SRC)/filters/FigureFilter.java \
$(SRC)/filters/FigureFilters.java \
$(SRC)/filters/EdgeColorFilter.java \
$(SRC)/filters/FillColorFilter.java \
$(SRC)/filters/LineFilter.java \
$(SRC)/filters/ShapeFilter.java \
$(SRC)/utils/FlyweightFactory.java \
$(SRC)/utils/IconFactory.java \
$(SRC)/utils/IconItem.java \
$(SRC)/utils/package-info.java \
$(SRC)/utils/PaintFactory.java \
$(SRC)/utils/StrokeFactory.java \
$(SRC)/utils/Vector2D.java \
$(SRC)/utils/CColor.java \
$(SRC)/history/package-info.java \
$(SRC)/history/HistoryManager.java \
$(SRC)/history/Memento.java \
$(SRC)/history/Originator.java \
$(SRC)/history/Prototype.java \
$(SRC)/widgets/EditorFrame.java \
$(SRC)/widgets/DrawingPanel.java \
$(SRC)/widgets/enums/OperationMode.java \
$(SRC)/widgets/enums/package-info.java \
$(SRC)/widgets/enums/TreeType.java \
$(SRC)/widgets/InfoPanel.java \
$(SRC)/widgets/JLabeledComboBox.java \
$(SRC)/widgets/package-info.java \
$(SRC)/widgets/TreesPanel.java

OTHER = $(SRC)/images/About.png \
$(SRC)/images/About_small.png \
$(SRC)/images/Black.png \
$(SRC)/images/Blue.png \
$(SRC)/images/Circle.png \
$(SRC)/images/Circle_small.png \
$(SRC)/images/Clear.png \
$(SRC)/images/Clear_small.png \
$(SRC)/images/ClearFilter.png \
$(SRC)/images/ClearFilter_small.png \
$(SRC)/images/Creation.png \
$(SRC)/images/Creation_small.png \
$(SRC)/images/Cyan.png \
$(SRC)/images/Dashed.png \
$(SRC)/images/Dashed_small.png \
$(SRC)/images/Delete.png \
$(SRC)/images/Delete_small.png \
$(SRC)/images/Details.png \
$(SRC)/images/Details_small.png \
$(SRC)/images/EdgeColor.png \
$(SRC)/images/EdgeColor_small.png \
$(SRC)/images/Edition.png \
$(SRC)/images/Edition_small.png \
$(SRC)/images/Ellipse.png \
$(SRC)/images/Ellipse_small.png \
$(SRC)/images/FillColor.png \
$(SRC)/images/FillColor_small.png \
$(SRC)/images/Filter.png \
$(SRC)/images/Filter_small.png \
$(SRC)/images/Green.png \
$(SRC)/images/Logo.png \
$(SRC)/images/Magenta.png \
$(SRC)/images/Move.png \
$(SRC)/images/Move_small.png \
$(SRC)/images/MoveDown.png \
$(SRC)/images/MoveDown_small.png \
$(SRC)/images/MoveUp.png \
$(SRC)/images/MoveUp_small.png \
$(SRC)/images/Ngon.png \
$(SRC)/images/Ngon_small.png \
$(SRC)/images/None.png \
$(SRC)/images/None_small.png \
$(SRC)/images/Orange.png \
$(SRC)/images/Others.png \
$(SRC)/images/Polygon.png \
$(SRC)/images/Polygon_small.png \
$(SRC)/images/Quit.png \
$(SRC)/images/Quit_small.png \
$(SRC)/images/Rectangle.png \
$(SRC)/images/Rectangle_small.png \
$(SRC)/images/Red.png \
$(SRC)/images/Redo.png \
$(SRC)/images/Redo_small.png \
"$(SRC)/images/Rounded Rectangle.png" \
"$(SRC)/images/Rounded Rectangle_small.png" \
$(SRC)/images/RoundedRectangle.png \
$(SRC)/images/RoundedRectangle_small.png \
$(SRC)/images/Solid.png \
$(SRC)/images/Solid_small.png \
$(SRC)/images/Star.png \
$(SRC)/images/Star_small.png \
$(SRC)/images/Style.png \
$(SRC)/images/Style_small.png \
$(SRC)/images/Tree.png \
$(SRC)/images/Tree_small.png \
$(SRC)/images/Undo.png \
$(SRC)/images/Undo_small.png \
$(SRC)/images/White.png \
$(SRC)/images/Yellow.png \
TP5.pdf

.PHONY : doc ps

# Les targets de compilation
# pour générer l'application
all : $(foreach name, $(MAIN), $(BIN)/$(name).class)

#règle de compilation générique
$(BIN)/%.class : $(SRC)/%.java
	$(JAVAC) -sourcepath $(SRC) -classpath $(BIN):$(CLASSPATH) -d $(BIN) $(CFLAGS) $<

# Edition des sources $(EDITOR) doit être une variable d'environnement
edit :
	$(EDITOR) $(SOURCES) Makefile &

# nettoyer le répertoire
clean :
	find bin/ -type f -name "*.class" -exec rm -f {} \;
	rm -rf *~ $(DOC)/* $(LISTDIR)/*

#realclean : clean
#	rm -f $(ARCHDIR)/*.$(ARCHFMT) 

# générer le listing
$(LISTDIR) : 
	mkdir $(LISTDIR)

ps : $(LISTDIR)
	$(A2PS) -2 --file-align=fill --line-numbers=1 --font-size=10 \
	--chars-per-line=100 --tabsize=4 --pretty-print \
	--highlight-level=heavy --prologue="gray" \
	-o$(LISTDIR)/$(OUTPUT).ps Makefile $(SOURCES)

pdf : ps 
	$(PS2PDF) $(LISTDIR)/$(OUTPUT).ps $(LISTDIR)/$(OUTPUT).pdf

# générer le listing lisible pour Gérard
bigps :
	$(A2PS) -1 --file-align=fill --line-numbers=1 --font-size=10 \
	--chars-per-line=100 --tabsize=4 --pretty-print \
	--highlight-level=heavy --prologue="gray" \
	-o$(LISTDIR)/$(OUTPUT).ps Makefile $(SOURCES)

bigpdf : bigps
	$(PS2PDF) $(LISTDIR)/$(OUTPUT).ps $(LISTDIR)/$(OUTPUT).pdf

# voir le listing
preview : ps
	$(GHOSTVIEW) $(LISTDIR)/$(OUTPUT); rm -f $(LISTDIR)/$(OUTPUT) $(LISTDIR)/$(OUTPUT)~

# générer la doc avec javadoc
doc : $(SOURCES)
	$(DOCP) -private -d $(DOC) -author -link $(LOCALLINK) $(SOURCES)
#	$(DOCP) -private -d $(DOC) -author -linkoffline $(WEBLINK) $(LOCALLINK) $(SOURCES)

# générer une archive de sauvegarde
$(ARCHDIR) : 
	mkdir $(ARCHDIR)

archive : pdf $(ARCHDIR)
	$(ARCH) $(ARCHDIR)/$(ARCHIVE)-$(DATE).$(ARCHFMT) $(SOURCES) $(LISTDIR)/*.pdf $(OTHER) $(BIN) Makefile $(FIGDIR)/*.pdf

# exécution des programmes de test
run : all
	$(foreach name, $(MAIN), $(JAVA) -classpath $(BIN):$(CLASSPATH) $(name) $(JAVAOPTIONS) )
