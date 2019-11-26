# BIO-fingerprint-GAN

## TODO
* Data Processing pro otisky prstů (velice podobně jako je to u těch psů)
    * Použít SOCOFing, labely použít typ prstu
    * První učit s 64x64 (oříznout 64x64, neměnit aspect ratio)
    * Zkusit učit s původními rozměry obrázků (103, 96) (úprava sítě?)
* Ukládat při učení checkpointy (ukládat na google drive + je umět načítat)
* Vizualizovat výstupy ze sítě během učení (ne jen nakonci, jak je to teď)
* Hyperparameter Tuning, zkusit různé objective function, upravit architekturu a celkově zkusit dosáhnout co nejlepších výsledků
* Zkusit učit na jiných datasetech

## Run
[![Open In Colab](https://colab.research.google.com/assets/colab-badge.svg)](https://colab.research.google.com/github/Luksalos/BIO-fingerprint-GAN/blob/master/dog_generating_GAN.ipynb)

## Datasets

#### SOCOFing
[Dostupné i na Kaggle](https://www.kaggle.com/ruizgara/socofing) (obsahuje duplicitní složky) |
[Publikace](https://arxiv.org/pdf/1807.10609.pdf) \
6000 otisků od 600 afrických osob (10x600). Obsahuje nějaké labely viz publikace.

#### CASIA
[Dostupné i zde](http://www.idealtest.org/dbDetailForUser.do?id=7) (nutná registrace) \
20000 rolovaných otisků od 500 osob (40x500)

#### PolyU Fingerprint Databases
[Dostupné i zde](http://www4.comp.polyu.edu.hk/~csajaykr/fingerprint.htm) (nutná registrace) \
1800 rolovaných otisků od 300 osob. \
Existuje také [jiný dataset](http://www4.comp.polyu.edu.hk/~biometrics/HRF/HRF_old.htm) od stejné skupiny, který nejspíš nejde stáhnout.

#### FVC2006 fingerprint database
[Dostupné zde](http://atvs.ii.uam.es/atvs/fvc2006.html) (nutná registrace učitele) |
[Popis datasetu](http://bias.csr.unibo.it/fvc2006/databases.asp)  \
1800 otisků od 140 osob (12x140). Dataset obsahuje 4 sady (vždy 1800) otisků získány různými sensory.

#### Datasets from NIST
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
    
    Implementaci jsem nenašel, ale mají přesně popsanou architekturu sítě, takže by neměl být problém ji zreplikovat.
    Ze tří popsaných publikací je tato rozhodně nejlepší.


## GANs SOTA and publications
* #### [StyleGAN - A Style-Based Generator Architecture for Generative Adversarial Networks (arXiv, Dec 2018 v1)](https://arxiv.org/abs/1812.04948)

* #### [BigGAN - Large Scale GAN Training for High Fidelity Natural Image Synthesis (arXiv, Sep 2018 v1)](https://arxiv.org/abs/1809.11096)

* #### [SAGAN - SSelf-Attention Generative Adversarial Networks (arXiv, May 2018 v1)](https://arxiv.org/abs/1805.08318)
    
* #### [Spectral Normalization for Generative Adversarial Networks (arXiv, Feb 2018)](https://arxiv.org/abs/1802.05957)
    Velkou mírou přispívá k stabilitě učení sítě. Použito např. v SAGAN
* #### [GANs Trained by a Two Time-Scale Update Rule Converge to a Local Nash Equilibrium (arXiv, Jun 2017 v1)](https://arxiv.org/abs/1706.08500)
    Použití různě velkého learning rate pro generátor a diskriminator.

* #### [Progressive Growing of GANs for Improved Quality, Stability, and Variation (arXiv, Oct 2017 v1)](https://arxiv.org/abs/1710.10196)
