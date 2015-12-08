# vychozi obrazky
SRC= logo.jpg obrazok1.jpg obrazok2.jpg obrazok3.jpg obrazok4.jpg schema.jpg schema2.jpg co.png cr.png op.png im.png ff.png ir.png result.png
# pozadovany format pro LaTeX
DEST=logo.eps obrazok1.eps obrazok2.eps obrazok3.eps obrazok4.eps schema.eps schema2.eps co.eps cr.eps op.eps im.eps ff.eps ir.eps result.eps

.SUFFIXES: .fig .eps .png .jpg

all: $(DEST)

.fig.eps:
	fig2dev -L eps $< $@

.png.eps:
	convert $< $@

.jpg.eps:
	convert $< $@
