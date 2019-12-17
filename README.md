# BIO: Generování syntetického otisku prstu pomocí GAN

[![Open In Colab](https://colab.research.google.com/assets/colab-badge.svg)](https://colab.research.google.com/github/Luksalos/BIO-fingerprint-GAN/blob/master/fingerprint_BigGAN.ipynb)

Tento projekt byl vypracován v rámci předmětu **biometrické systémy** na Fakultě informačních technologií Vysokého učení technického v Brně. Cílem projektu je generovat syntetické otisky prstu pomocí generativní adversiální sítě (GAN).

## Úvod

GAN je typ strojového učení představený v roce 2014 používaný pro generování nových dat z určité distribuce [1]. Princip spočívá v soutěžení dvou neuronových sítí: generátoru nových dat a diskriminátoru, který se snaží odlišit reálná data od těch vygenerovaných.

## Hledání datasetu

Prvním krokem bylo nalezení vhodných trénovacích dat. Na internetu již bohužel nejsou k nalezení rozsáhlé datasety amerického NISTu. Po zvážení několika kandidátů [2] [3] [4] [5] jsme zvolili dataset SOCOFing dostupný na Kaggle [6] pro relativně kvalitní snímky otisků a kompaktnost.

Dataset jsme předzpracovali - bylo třeba oříznout rámečky kolem otisků prstů a změnit rozměry snímků tak, aby šly snadno integrovat do nějaké z existujících architektur GAN. V názvech obrázků datasetu byly zakódované informace o typu prstu a pohlaví subjektu, které jsme extrahovali a při trénování použili pro kategorizaci dat.

## Volba architektury

Po nastudování mnoha *state of the art* technik a architektur GAN [7] [8] [9] [10] [11] [12] [13] [14] [15] [16] [17] a několika publikací zabývajících se generováním otisků prstů [18] [19] [20] jsme jako základ našeho modelu jsme zvolili existující implementaci [21] architektury BigGAN.

Tu jsme pozměnili tak, aby dokázala pracovat s šedotónovými obrázky ve větším rozlišení (96x96 pixelů oproti původním 64x64 pixelům). Zároveň jsme vybudovali infrastrukturu pro ukládání a snadné načítání naučených modelů.

* TODO: popsat, jak zhruba funguje a co přesně jsme na ní pozměnili

## Učicí infrastruktura

Náš model jsme implementovali v Pythonu, formou Jupyter Notebooku. Tento formát umožnuje rychlé prototypování a přehlednou vizualizaci dat.

Zpočátku bylo možné menší/jednodušší verze našeho modelu učit na běžných stolních počítačích disponujících grafickou kartou NVIDIA GTX 1060 s 6 GB VRAM. Po zvětšení našeho modelu bylo ale třeba více paměti a proto jsme náš projekt hostovali na Google Colab - službě, jež zdarma poskytuje výpočetní GPU výkon.

Subjektivně kvalitní obrázky náš model generuje po cca 10 tisících iteracích učení, které na jedné grafické kartě NVIDIA TESLA P100 trvá zhruba 5 hodin.

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

[1] Goodfellow et al., *Generative Adversarial Networks*, 2014.

[2] CASIA FingerprintV5, dostupné zde: [idealtest.org](http://www.idealtest.org/dbDetailForUser.do?id=7) (po registraci).

[3] Hong Kong Polytechnic University Fingerprint Images Database, dostupné zde: [comp.polyu.edu.hk](http://www4.comp.polyu.edu.hk/~csajaykr/fingerprint.htm) (po registraci).

[4] FVC2006, dostupné zde: [atvs.ii.uam.es](http://atvs.ii.uam.es/atvs/fvc2006.html) (po registraci učitele)

[5] NIST dataset SD04, dostupné na internetu.

[6] SOCOFing, dostupné zde: [kaggle.com](https://www.kaggle.com/ruizgara/socofing).

[7] Brock et al. *Large Scale GAN Training for High Fidelity Natural Image Synthesis*, 2019.

[8] He et al. *Deep residual learning for image recognition*, 2016.

[9] Jolicoeur-Martineau et al. *The relativistic discriminator: a key element missing from standard GAN*, 2019.

[10] Odena et al. *Conditional image synthesis with auxiliary classifier GANs*, 2017.

[11] Zhang et al. *Self-attention generative adversarial networks*, 2018.

[12] Miyato et al. *Spectral normalization for generative adversarial networks*, 2018.

[13] Karras et al. *Progressive Growing of GANs for Improved Quality, Stability, and Variation*, 2018.

[14] Karras et al. *A Style-Based Generator Architecture for Generative Adversarial Networks*, 2018.

[15] Brock et al. *Large Scale GAN Training for High Fidelity Natural Image Synthesis*, 2019.

[16] Zhang et al. *Self-Attention Generative Adversarial Networks*, 2019.

[17] Heusel et al. *GANs Trained by a Two Time-Scale Update Rule Converge to a Local Nash Equilibrium*, 2018.

[18] Minaee et al. *Finger-GAN: Generating Realistic Fingerprint Images Using Connectivity Imposed GAN*, 2018.

[19] Bontrager et al. *DeepMasterPrints: Generating MasterPrints for Dictionary Attacks via Latent Variable Evolution*, 2017.

[20] Cao et al. *Fingerprint Synthesis: Evaluating Fingerprint Search at Scale*, 2018.

[21] RaLS AC-BigGAN with MinibatchStddev, dostupné zde: [kaggle.com](https://www.kaggle.com/yukia18/sub-rals-ac-biggan-with-minibatchstddev).

## Evaluation

- [Xu Q. et al., 2018] [An empirical study on evaluation metrics of generative adversarial networks](https://arxiv.org/pdf/1806.07755.pdf)
- [Borji A., 2018] [Pros and Cons of GAN Evaluation Measures](https://arxiv.org/pdf/1802.03446.pdf)
- [Shmelkov K., Schmid C., Alahari K, 2018] [How good is my GAN?](https://hal.inria.fr/hal-01850447/document)
