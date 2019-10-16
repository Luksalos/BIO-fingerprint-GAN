# BIO-fingerprint-GAN

## Datasets

#### SOCOFing
[Stáhnout](https://drive.google.com/open?id=1netSFAA3xMxTUw7AwgnmUDvIfSUTPPME) |
[Dostupné i na Kaggle](https://www.kaggle.com/ruizgara/socofing) (obsahuje duplicitní složky) |
[Publikace](https://arxiv.org/pdf/1807.10609.pdf) \
6000 otisků od 600 afrických osob (10x600). Obsahuje nějaké labely viz publikace.

#### CASIA
[Stáhnout](https://drive.google.com/open?id=1-_qpfFk3nfBn6rd01PHHKJhG2pi9CImv) |
[Dostupné i zde](http://www.idealtest.org/dbDetailForUser.do?id=7) (nutná registrace) \
20000 rolovaných otisků od 500 osob (40x500)

#### PolyU Fingerprint Databases
[Stáhnout](https://drive.google.com/open?id=132CaZCbq4Z92KKo2oBWM4p54wC3BhpCN) |
[Dostupné i zde](http://www4.comp.polyu.edu.hk/~csajaykr/fingerprint.htm) (nutná registrace) \
1800 rolovaných otisků od 300 osob. \
Existuje také [jiný dataset](http://www4.comp.polyu.edu.hk/~biometrics/HRF/HRF_old.htm) od stejné skupiny, který nejspíš nejde stáhnout.

#### FVC2006 fingerprint database
[Dostupné zde](http://atvs.ii.uam.es/atvs/fvc2006.html) (nutná registrace učitele) |
[Popis datasetu](http://bias.csr.unibo.it/fvc2006/databases.asp)  \
1800 otisků od 140 osob (12x140). Dataset obsahuje 4 sady (vždy 1800) otisků získány různými sensory.

#### Datasets from NIST
[Stáhnout SD04](https://drive.google.com/open?id=1vkwb87E1_fuXUG9q2KHI2mBMWgspxRg_) |
[Info](https://www.nist.gov/itl/iad/image-group/resources/biometric-special-databases-and-software) \
Další části nedostupné. Měly by to být desetitisíce otisků (SD14 obsahuje 54K otisků). \
Nelze je stáhnout někde jinde? Nemá je někdo z učitelů už stažené?  

## Related Work
* #### [Finger-GAN (arXiv, Dec 2018)](https://arxiv.org/abs/1812.10482)  
    Používají "obyčejný" deep convolutional GAN (DC-GAN). 
    Zmiňují, že je problém dosáhnout spojitých čar u vygenerovaných otisků prstů, proto
    použili pro regularizaci total variation (TV). 
    Generují obrázky 64x64.  
    
    Použité datasety:
    1. FVC 2006 Fingerprint Database (12x140)
    2. PolyU Fingerprint Databases (5x300), (použité zvlášť).  
    
    FID score, jak sami zmiňují mají poměrně malé (70.5)  
    Implementaci jsem nenašel.
    
    Nekvalitní publikace, zmiňují že GANy používají pro generování otisků jako první, 
    což rozhodně neni pravda, viz. další dvě popsané publikace.
    
* #### [DeepMasterPrints (arXiv, Oct 2018 v4)](https://arxiv.org/abs/1705.07386)  
    Cílem publikace je ukázat jak lze napadnout malé čtečky otisků prstů (na mobilních telefonech).
    Učí síť generovat (částečné) otisky prstů. Po naučení sítě hledají v jejím latent space takové otisky, které 
    se nejlépe hodí pro slovníkový útok (bez znalosti otisků, které jsou u čtečky autorizovány).  
    Používají síť popsanou v publikaci [Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks](https://arxiv.org/abs/1511.06434)
    Je to WGAN, Wasserstein loss function, RMSProp lr=0.00005, batchSize=64, laten variable vector=64
    
    Použité datasety:
    1. NIST Special Database 9 (10x5400)  
    2. FingerPass DB7 dataset (12 částečných otisku x720) (použité zvlášť)  
    
     Implementaci jsem nenašel.
    
    
* #### [Fingerprint Synthesis (IEEE, Feb 2018)](https://ieeexplore.ieee.org/document/8411200)
    Používají I-WGAN. Pro lepší výsledky a snazší trénink mají v architektuře pro předtrénování generátoru convolutional auto-encoder.  
    Natrénovat např DC-GAN pro generování 512x512 obrázků je bez optimalizací (např.: progressive GAN) takřka nemožné.    
    Generují otisky o velikosti 512x512 (500 DPI). 
    
    Použité datasety:
    1. 250K rolovaných otisků prstů od nezmíněné forézní agentury. Odkazují se na [tento článek](https://ieeexplore.ieee.org/document/8272728), 
    kde byl použit stejný dataset
    
    Implementaci jsem nenašel, ale mají přesně popsanou architekturu sítě, takže by neměl být problém ji zreplikovatproblém ji zrepli.
    popsaných
    Ze tří popsaných publikací je tato rozhodně nejlepší.

## TODO
Nastudovat jak přesně funguje total variation a popřemýšlet nad regularizací pro docílení kvalitních obrázků.
