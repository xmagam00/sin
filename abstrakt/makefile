NAME=1-xmecav00-xmagam00-50-50
$(NAME).pdf: $(NAME).dvi
	dvipdf $(NAME).dvi

$(NAME).dvi: $(NAME).tex
	latex $(NAME).tex
	latex $(NAME).tex

clean:
	rm -f $(NAME).dvi $(NAME).aux $(NAME).log $(NAME).out $(NAME).pdf

