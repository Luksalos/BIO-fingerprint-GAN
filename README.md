# BIO: generování otisků prstů s GAN

## TODO
- [x] Ukládat více obrázků v průběhu učení (obrázky můžou být obdelníkové, vždy ale budou stejné rozměry)
- [x] Vizualizace dat pro učení sítě (po processingu)
- [] Zdokumentovat získání datasetu a jeho načtení
- [x] Ukládat checkpointy a obrázky z průběhu učení na google drive. 
    - [x] Zajistit i možnost načtení checkpointu z google drive (dobře zdokumentovat). 
    - [] Ověřit, jestli neni potřeba ukládat zvlášť i jiné časově závislé parametry použité pro učení sítě.
       - Ten LR by se kvůli decay měl určitě ukládat, ale ta decay funkce se mi zdá být nějaká divná ...
- [x] Úprava sítě pro naše data
- [x] Úprava sítě pro obdelníkové obrázky
- [] Vyhodnocení kvality a diversity generovaných dat. Zvolit vhodné metriky/metody
    - porovnat s ostatními (viz. publikace níže)
- [] Učit síť 64x64 s cropingem
- [] Učit síť 80x80
- [] Učit síť 64x64, nebo 80x80 s random cropingem
- [] Učit síť 96x96 
    - Pracuji na tom, Lukáš
- [] Učit síť s 1 kanálem pro barvu (misto RGB) 
    - Padá to už na A.Normalize (lze napsat vlastní funkci), ale bude potřeba více drobných úprav.
- [x] Vyčistit repo (smazat SAGAN) 
- [] Přidat licenci a zkontrolovat stávající použití v Colabu
- [] Přesunout zdroje v popisu architektury do referencí v dokumentaci

## Run
[![Open In Colab](https://colab.research.google.com/assets/colab-badge.svg)](https://colab.research.google.com/github/Luksalos/BIO-fingerprint-GAN/blob/master/fingerprint_BigGAN.ipynb)

## Popis použité architektury:
- Síť založena na BigGAN [Brock et al., 2019]
- Spectral Normalization [Miyato et al., 2018] pro lepší stabilitu učení.
- Self-attention [[Zhang et al., 2018]] - lepší kvalita
- Auxiliary classifier [Odena et al., 2017] - lepší kvalita a stabilita učení.
- Residual blocks [He et al., 2016] - Umožňuje efektivní trénování hlubokých sítí (lepší propagace gradientu), lepší kvalita
- Minibatch Standard Deviation [Karras et al., 2018] pro lepší rozmanitost generovaných dat.
- Loss: RaLS [Jolicoeur-Martineau et al., 2019] s váhovanou auxiliary classification loss [Odena et al., 2017]
- Uniform input noise na reálné obrázky
- Shared embedding 

### Zdroje
- [Brock et al., 2019] Andrew Brock, Jeff Donahue, Karen Simonyan. Large Scale GAN Training for High Fidelity Natural 
Image Synthesis. In ICLR, 2019.
- [He et al., 2016] Kaiming He, Xiangyu Zhang, Shaoqing Ren, and Jian Sun. Deep residual learning 
for image recognition. In CVPR, 2016.
- [Jolicoeur-Martineau et al., 2019] Alexia Jolicoeur-Martineau. The relativistic discriminator: a key element missing 
from standard GAN. in ICLR, 2019.
- [Odena et al., 2017] Augustus Odena, Christopher Olah, and Jonathon Shlens. Conditional image synthesis 
with auxiliary classifier GANs. In ICML, 2017.
- [Zhang et al., 2018] Han Zhang, Ian Goodfellow, Dimitris Metaxas, and Augustus Odena. Self-attention generative
adversarial networks. In arXiv preprint arXiv:1805.08318, 2018.
- [Miyato et al., 2018] Takeru Miyato, Toshiki Kataoka, Masanori Koyama, and Yuichi Yoshida. Spectral normalization
for generative adversarial networks. In ICLR, 2018.
- [Karras et al., 2018] Tero Karras, Timo Aila, Samuli Laine, Jaakko Lehtinen. Progressive Growing of GANs for 
Improved Quality, Stability, and Variation. In ICLR, 2018

## Datasets

- SOCOFing | [Dostupné na Kaggle](https://www.kaggle.com/ruizgara/socofing) (obsahuje duplicitní složky) | [Publikace](https://arxiv.org/pdf/1807.10609.pdf) | 6000 otisků od 600 afrických osob (10x600)
- CASIA | [Dostupné zde](http://www.idealtest.org/dbDetailForUser.do?id=7) (nutná registrace) | 20000 rolovaných otisků od 500 osob (40x500)
- Hong Kong Polytechnic University | [Dostupné zde](http://www4.comp.polyu.edu.hk/~csajaykr/fingerprint.htm) (nutná registrace) | 1800 rolovaných otisků od 300 osob
  - Existuje také [jiný dataset](http://www4.comp.polyu.edu.hk/~biometrics/HRF/HRF_old.htm) od stejné skupiny, který ale nejspíš nejde stáhnout.
- FVC2006 | [Dostupné zde](http://atvs.ii.uam.es/atvs/fvc2006.html) (nutná registrace učitele) | [Popis datasetu](http://bias.csr.unibo.it/fvc2006/databases.asp) | 1800 otisků od 140 osob (12x140)
  - Dataset obsahuje 4 sady (vždy 1800) otisků získány různými sensory.
- NIST datasets | [Info](https://www.nist.gov/itl/iad/image-group/resources/biometric-special-databases-and-software)
  - Další části nedostupné. Měly by to být desetitisíce otisků (SD14 obsahuje 54K otisků).

## Related Work

### [Finger-GAN (arXiv, Dec 2018)](https://arxiv.org/abs/1812.10482)
Používají "obyčejný" deep convolutional GAN (DC-GAN).
Zmiňují, že je problém dosáhnout spojitých čar u vygenerovaných otisků prstů, proto použili pro regularizaci total variation (TV).
Generují obrázky 64x64.

Použité datasety:
1. FVC 2006 Fingerprint Database (12x140)
2. PolyU Fingerprint Databases (5x300), (použité zvlášť).  

FID score, jak sami zmiňují mají poměrně malé (70.5).
Implementaci jsem nenašel.

Nekvalitní publikace, zmiňují že GANy používají pro generování otisků jako první, což rozhodně neni pravda, viz. další dvě popsané publikace.

### [DeepMasterPrints (arXiv, Oct 2018 v4)](https://arxiv.org/abs/1705.07386)  
Cílem publikace je ukázat jak lze napadnout malé čtečky otisků prstů (na mobilních telefonech).

Učí síť generovat (částečné) otisky prstů. Po naučení sítě hledají v jejím latent space takové otisky, které 
se nejlépe hodí pro slovníkový útok (bez znalosti otisků, které jsou u čtečky autorizovány).

Používají síť popsanou v publikaci [Unsupervised Representation Learning with Deep Convolutional Generative Adversarial Networks](https://arxiv.org/abs/1511.06434). Je to WGAN, Wasserstein loss function, RMSProp lr=0.00005, batchSize=64, laten variable vector=64

Použité datasety:
1. NIST Special Database 9 (10x5400)  
2. FingerPass DB7 dataset (12 částečných otisku x720) (použité zvlášť)  

Implementace nenalezena.
    
### [Fingerprint Synthesis (IEEE, Feb 2018)](https://ieeexplore.ieee.org/document/8411200)
 Používají I-WGAN. Pro lepší výsledky a snazší trénink mají v architektuře pro předtrénování generátoru convolutional auto-encoder.

Natrénovat např DC-GAN pro generování 512x512 obrázků je bez optimalizací (např.: progressive GAN) takřka nemožné.
Generují otisky o velikosti 512x512 (500 DPI). 
    
Použité datasety:
1. 250K rolovaných otisků prstů od nezmíněné forézní agentury. Odkazují se na [tento článek](https://ieeexplore.ieee.org/document/8272728), kde byl použit stejný dataset.

Implementace nenalezena, ale mají přesně popsanou architekturu sítě, takže by neměl být problém ji zreplikovat.
Ze tří popsaných publikací je tato rozhodně nejlepší.

## GANs SOTA and publications
- [StyleGAN - A Style-Based Generator Architecture for Generative Adversarial Networks (arXiv, Dec 2018 v1)](https://arxiv.org/abs/1812.04948)
- [BigGAN - Large Scale GAN Training for High Fidelity Natural Image Synthesis (arXiv, Sep 2018 v1)](https://arxiv.org/abs/1809.11096)
- [SAGAN - SSelf-Attention Generative Adversarial Networks (arXiv, May 2018 v1)](https://arxiv.org/abs/1805.08318)
- [Spectral Normalization for Generative Adversarial Networks (arXiv, Feb 2018)](https://arxiv.org/abs/1802.05957) - velkou mírou přispívá k stabilitě učení sítě (použito např. v SAGAN)
- [GANs Trained by a Two Time-Scale Update Rule Converge to a Local Nash Equilibrium (arXiv, Jun 2017 v1)](https://arxiv.org/abs/1706.08500) - použití různě velkého learning rate pro generátor a diskriminator
- [ProGAN - Progressive Growing of GANs for Improved Quality, Stability, and Variation (arXiv, Oct 2017 v1)](https://arxiv.org/abs/1710.10196)
