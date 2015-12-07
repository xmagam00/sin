NAME=vysledok
$(NAME).pdf: $(NAME).dvi
	dvipdf $(NAME).dvi

$(NAME).dvi: $(NAME).tex
	latex $(NAME).tex
	latex $(NAME).tex

clean:
	rm -f $(NAME).dvi $(NAME).aux $(NAME).log $(NAME).out $(NAME).pdf

