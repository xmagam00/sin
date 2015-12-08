#!/bin/bash
mkdir output
cd dokumentacia
make clean;
make;
mv vysledok.pdf dokumentacia.pdf;
cd ..;
cp dokumentacia/dokumentacia.pdf output;
cd projekt;
rm -fr lib;
rm -fr bin;
ant clean;
cd ..;
cp -R projekt output;
zip 1-xmagam00-xmecav00-50-50 output/dokumentacia.pdf output/projekt/*;
rm -fr output
