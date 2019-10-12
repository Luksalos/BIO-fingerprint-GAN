# BIO-fingerprint-GAN

## Datasets

* #### SOCOFing
    [Ke stažení na Kaggle](https://www.kaggle.com/ruizgara/socofing)  
[Publikace](https://arxiv.org/pdf/1807.10609.pdf)  
6000 snímků od 600 afrických osob.
Obsahuje nějaké labely viz. publikace

* #### PolyU Fingerprint Databases
    [Link na stránku s datasetem](http://www4.comp.polyu.edu.hk/~csajaykr/fingerprint.htm)  
[Jiný dataset od stejné skupiny, nevím ale jak stáhnout](http://www4.comp.polyu.edu.hk/~biometrics/HRF/HRF_old.htm)  
1800 otisků získaných rolováním od 300 osob.  
Stažení po vyplnění formuláře.

* #### FVC2006 fingerprint database
    [Stažení po vyžádání e-mailem](http://atvs.ii.uam.es/atvs/fvc2006.html)  
[Popis datasetu](http://bias.csr.unibo.it/fvc2006/databases.asp)  
1800 otisků od 140 osob (12 otisků na osobu).  
Dataset obsahuje 4 sady (vždy 1800) otisků získány různými sensory.  
Stažení po vyžádání e-mailem.

* #### Datasets from NIST
    [Link](https://www.nist.gov/itl/iad/image-group/resources/biometric-special-databases-and-software)  
Dočasně nedostupné.  
Nelze je stáhnout někde jinde, nebo nemá je někdo stažené, že by nám je poskytnul?

## Similar research
* [Finger-GAN (arXiv, Dec 2018)](https://arxiv.org/abs/1812.10482)  
    Používají "obyčejný" deep convolutional GAN (DC-GAN). 
    Zmiňují, že je problém dosáhnout spojitých čar u vygenerovaných otisků prstů, proto
    použili pro regularizaci total variation (TV). 
    Generují obrázky 64x64. 
    Používají datasety: FVC 2006 Fingerprint Database a PolyU Fingerprint Databases (zvlášť).
    FID score, jak sami zmiňují mají poměrně malé (70.5)
    
    Implementaci jsem nenašel.
* [DeepMasterPrints (arXiv, Oct 2018 v4)](https://arxiv.org/abs/1705.07386)  
* [Fingerprint Synthesis (IEEE, Feb 2018)](https://ieeexplore.ieee.org/document/8411200)

## TODO
Nastudovat jak přesně funguje total variation a popřemýšlet na regularizací pro docílení kvalitních obrázků.
