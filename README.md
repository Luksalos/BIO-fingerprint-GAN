# BIO: Generování syntetického otisku prstu pomocí GAN

[![Open In Colab](https://colab.research.google.com/assets/colab-badge.svg)](https://colab.research.google.com/github/Luksalos/BIO-fingerprint-GAN/blob/master/fingerprint_BigGAN.ipynb)

Tento projekt byl vypracován v rámci předmětu **biometrické systémy** na Fakultě informačních technologií Vysokého učení technického v Brně. Cílem projektu je generovat syntetické otisky prstu pomocí generativní adversiální sítě (GAN).

## Úvod

GAN je typ strojového učení představený v roce 2014 používaný pro generování nových dat z určité distribuce [1]. Princip spočívá v soutěžení dvou neuronových sítí: generátoru nových dat a diskriminátoru, který se snaží odlišit reálná data od těch vygenerovaných.

## Hledání datasetu

Prvním krokem bylo nalezení vhodných trénovacích dat. Na internetu již bohužel nejsou k nalezení rozsáhlé datasety amerického NISTu. Po zvážení několika kandidátů [2] [3] [4] [5] jsme zvolili dataset SOCOFing dostupný na Kaggle [6] pro relativně kvalitní snímky otisků a kompaktnost. Dataset jsme předzpracovali - bylo třeba oříznout rámečky kolem otisků prstů a změnit rozměry snímků tak, aby šly snadno integrovat do nějaké z existujících architektur GAN. V názvech obrázků datasetu byly zakódované informace o typu prstu a pohlaví subjektu, které jsme extrahovali a při trénování použili pro kategorizaci dat.

## Volba architektury

Po nastudování řady různých achitektur [6] [7] [8] [9] jsme jako základ našeho modelu jsme zvolili existující implementaci [10] architektury BigGAN. Tu jsme pozměnili tak, aby dokázala pracovat s šedotónovými obrázky ve větším rozlišení (96x96 pixelů oproti původním 64x64 pixelům). Zároveň jsme vybudovali infrastrukturu pro ukládání a snadné načítání naučených modelů.

## Učící infrastruktura

Náš model jsme implementovali v Pythonu, formou Jupyter Notebooku. Tento formát umožnuje rychlé prototypování a přehlednou vizualizaci dat.

Zpočátku bylo možné menší/jednodušší verze našeho modelu učit na běžných stolních počítačích disponujících grafickou kartou NVIDIA GTX 1060 s 6 GB VRAM. Po zvětšení našeho modelu bylo ale třeba více paměti a proto jsme náš projekt hostovali na Google Colab - službu, jež zdarma poskytuje výpočetní GPU výkon.

Zajímavé obrázky náš model generuje po cca 10 tisících iteracích, které na jedné grafické kartě NVIDIA TESLA P100 běží zhruba 5 hodin.

## Vyhodnocení

Vyhodnocování kvality GAN je stále oblastní aktivního výzkumu a prozatím neexistuje standardizovaná generická metoda. Často používané metriky jsou Inception Score [11] a Fréchet Inception Distance [12], které využívají model naučený na datasetu ImageNet. Jelikož tento dataset ale neobsahuje žádné otisky prstů, použití těchto modelů pro náš účel nejspíš nedává smysl.

Podle některých zdrojů [13] může být vhodnou metrikou např. manuální porovnávání vygenerovaných obrázků s jejich nejbližšími sousedy z trénovacího datasetu (ve smyslu Nearest Neighbor klasifikátoru). Touto metodou by se dalo zjistit, jak unikátní data je generátor schopný vytvořit. Při použití Nearest Neighbor jsme sice získali páry otisků, které jsou si tvarově podobné, zpravidla však byly markantově naprosto odlišné.

Pokusili jsme se tedy hledat páry obrázků na základě podobnosti markantů. Pro tento účel jsme použili Java knihovnu SourceAFIS [14], která ovšem párovala diametrálně odlišné otisky. Doposud jsme nezjistili příčinu špatné funkčnosti. Je možné, že detektoru markantů dělaly potíže lehké artefakty, které se objevují na vygenerovaných obrázcích.

## Závěr

Lze říci, že se nám podařilo vytvořit funkční generátor otisků prstů založený na GAN. Při manuální inspekci vygenerované obrázky v drtivé většině případů vypadají jako reálné otisky a obsahují rozpoznatelné markanty. Je však třeba více prozkoumat možnosti kvantitativního vyhodnocení generátorů.

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

---

[1] Goodfellow I.J. et al., *Generative Adversarial Networks*, 2014.

[2] CASIA FingerprintV5, Dostupné zde: [idealtest.org](http://www.idealtest.org/dbDetailForUser.do?id=7) (po registraci).

[3] Hong Kong Polytechnic University Fingerprint Images Database, Dostupné zde: [comp.polyu.edu.hk](http://www4.comp.polyu.edu.hk/~csajaykr/fingerprint.htm) (po registraci).

[4] FVC2006, Dostupné zde: [atvs.ii.uam.es](http://atvs.ii.uam.es/atvs/fvc2006.html) (po registraci učitele)

[5] NIST dataset SD04, Dostupné na internetu.

[6] SOCOFing, Dostupné zde: [kaggle.com](https://www.kaggle.com/ruizgara/socofing).

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

## Evaluation

Žádný standardizovaný vyhodnocovací model. Často se používá např. Inception Score, které ale využívá síť naučenou na ImageNetu, což je pro otisky prstů nejspíš irelevantní. Doporučuje se 1NN ve spojením s manuálním vyhodnocením.

- [Xu Q. et al., 2018] [An empirical study on evaluation metrics of generative adversarial networks](https://arxiv.org/pdf/1806.07755.pdf)
- [Borji A., 2018] [Pros and Cons of GAN Evaluation Measures](https://arxiv.org/pdf/1802.03446.pdf)
- [Shmelkov K., Schmid C., Alahari K, 2018] [How good is my GAN?](https://hal.inria.fr/hal-01850447/document)

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
