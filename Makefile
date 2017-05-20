
release: clean min build-css zip


clean:
	rm ./game.zip
	lein clean

min:
	lein cljsbuild once min

build-css:
	lein garden once

src-path = resources/public
zip:
	cd $(src-path) && zip -r game.zip index.html js/ css/ img/
	mv $(src-path)/game.zip .
