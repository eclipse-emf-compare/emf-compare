textfiles = $(wildcard rst/*.rst)
htmlfiles := $(patsubst rst/%rst,html/%html,$(textfiles))

html/%.html: rst/%.rst
		rst2html.py  $< > $@
		
html: $(htmlfiles)

clean: 
		 @rm -f $(htmlfiles)
