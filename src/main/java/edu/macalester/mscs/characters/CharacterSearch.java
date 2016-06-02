package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.lang.reflect.Array;
import java.io.*;
import java.util.*;

/**
 * The mynotoar list only has first occurrence of a character. So we don't know whether a character in AGOT actually
 * appears in ACOK. The point of this code is to go through the previous characters lists (including appendices) and then
 * suggest which characters to include in the next book. This is not foolproof-- with both false positives and
 * false negatives. But it cuts out a lot of manual work.
 *
 * Created by abeverid on 5/27/16.
 */
public class CharacterSearch {

    public static final int SHORT_CAT_BOOK_INDEX = 0;
    public static final int SHORT_CAT_ID_INDEX = 1;
    public static final int SHORT_CAT_NAME_INDEX = 2;
    public static final int SHORT_CAT_TITLE_INDEX = 3;
    public static final int SHORT_CAT_FORENAME_INDEX = 4;
    public static final int SHORT_CAT_SURNAME_INDEX = 5;
    public static final int SHORT_CAT_FORMER_SURNAME_INDEX = 6;
    public static final int SHORT_CAT_ALIAS_INDEX = 7;


    /**
     * These were left over wiki names after reconciling the mynotoar names.
     */
    public static final String COK_WIKI_ONLY_NAMES =
            "Aegon-Frey-(son-of-Stevron),Aegon-Frey-(son-of-Aenys),Aemon-Estermont,Aemon-Rivers,Aladale-Wynch,Alan,Alannys-Harlaw,Albar-Royce,Albett,Alekyne-Florent,Alerie-Hightower,Alesander-Frey,Alester-Norcross,All-for-Joffrey,Alyn-Ambrose,Alyn-Estermont,Alyn-Frey,Alyn-Haigh,Alys-Frey,Alyssa-Blackwood,Alyx-Frey,Amarei-Crakehall,Ambrode,Amerei-Frey,Andar-Royce,Andrew-Estermont,Andrey-Charlton,Androw-Frey,Annara-Farring,Anya-Waynwood,Areo-Hotah,Arianne-Martell,Arwood-Frey,Arwyn-Frey,Baelor-Blacktyde,Bannen,Barth-(brewer),Barthogan-Stark,Bellena-Hawick,Benedict-Broom,Benfrey-Frey,Berena-Hornwood,Bethany-Rosby,Betharios-of-Braavos,Blane,Bowen-Marsh,Bradamar-Frey,Brandon-Stark,Brandon-Tallhart,Bryan-Frey,Butterbumps,Caleotte,Carolei-Waynwood,Cerenna-Lannister,Cersei-Frey,Cheyk,Clydas,Colin-Florent,Colmar-Frey,Cotter-Pyke,Cuger,Cynthea-Frey,Dacey-Mormont,Dafyn-Vance,Damon-Paege,Damon-Vypren,Danwell-Frey,Dareon,Darlessa-Marbrand,Deana-Hardyng,Della-Frey,Dickon-Frey,Donnel-Haigh,Donnel-Waynwood,Eddara-Tallhart,Edric-Dayne,Edwyn-Frey,Elron,Elyana-Vypren,Emberlei-Frey,Eon-Hunter,Erena-Glover,Eroeh,Erren-Florent,Esgred,Forley-Prester,Galt,Gariss,Garrett-Flowers,Garse-Flowers,Garse-Goodbrook,Garth-Tyrell,Gawen-Glover,Gawen-Westerling,Gelmarr,Gerion-Lannister,Gormon-Tyrell,Gueren,Halmon-Paege,Hobb,Hosman-Norcross,Hoster-Frey,Igon-Vyrwel,Jacks,Janei-Lannister,Janna-Tyrell,Jaqen-H'ghar,Jarmen-Buckwell,Jeren,Jeyne-Beesbury,Jeyne-Goodbrook,Jeyne-Lydden,Jeyne-Rivers,Joanna-Lannister,Jon-Brax,Jon-Wylde,Jonos-Frey,Joy-Hill,Joyeuse-Erenford,Jurne,Jyanna-Frey,Kedge-Whiteye,Kyra-Frey,Leslyn-Haigh,Lomas-Estermont,Lomys,Lothar-Frey,Luceon-Frey,Lucias-Vypren,Lyle-Crakehall,Lyman-Darry,Lyn-Corbray,Lyonel-(knight),Lythene-Frey,Maege-Mormont,Maegelle-Frey,Malwyn-Frey,Marianne-Vance,Marissa-Frey,Mariya-Darry,Marsella-Waynwood,Marwyn-Belmore,Masha-Heddle,Mathis-Frey,Matthar,Melara-Crane,Meldred-Merlyn,Melesa-Crakehall,Melessa-Florent,Mellara-Rivers,Melwys-Rivers,Merianne-Frey,Merrell-Florent,Mina-Tyrell,Morton-Waynwood,Morya-Frey,Moryn-Tyrell,Mullin,Murch-(Winterfell),Mya-Stone,Mychel-Redfort,Mylenda-Caron,Myranda-Royce,Myrielle-Lannister,Nestor-Royce,Norne-Goodbrother,Oberyn-Martell,Olenna-Redwyne,Omer-Florent,Orell,Oro-Tendyris,Osmund-Frey,Othell-Yarwyck,Pate-of-the-Blue-Fork,Patrek-Vance,Perra-Frey,Perriane-Frey,Petyr-Frey,Polliver,Pypar,Quent,Quentyn-Martell,Raymund-Frey,Red-Rolfe,Renly-Norcross,Rhaegar-Frey,Rhaego,Rhea-Florent,Rickard-Wylde,Robert-Brax,Robert-Brax-(son-of-Flement),Robert-Frey,Robert-Frey-(son-of-Raymund),Rodrik-Harlaw,Roslin-Frey,Ryam-Florent,Rycherd-Crane,Ryella-Frey,Ryella-Royce,Ryger-Rivers,Rylene-Florent,Sallei-Paege,Sandor-Frey,Sarra-Frey,Sarya-Whent,Satin,Serra-Frey,Shirei-Frey,Simon-Toyne,Stannis-Seaworth,Steffon-Frey,Steffon-Seaworth,Sybelle-Glover,Sylwa-Paege,Symond-Frey,Timett-(father),Todder,Tom;Too,Tyana-Wylde,Tysane-Frey,Tyta-Frey,Tytos-Frey,Tywin-Frey,Vickon-Greyjoy,Vortimer-Crane,Walda-Frey-(daughter-of-Edwyn),Walda-Frey-(daughter-of-Lothar),Walda-Frey-(daughter-of-Walton),Walda-Frey-(daughter-of-Rhaegar),Walda-Rivers,Walder-Brax,Walder-Frey-(son-of-Ryman),Walder-Frey-(son-of-Emmon),Walder-Goodbrook,Walder-Haigh,Walder-Vance,Waldon-Wynch,Walton-Frey,Wendel-Frey,Whalen-Frey,Will,Willamen-Frey,Willas-Tyrell,Willem-Frey,Wynafrei-Whent,Zachery-Frey,Zhoe-Blanetree,Zia-Frey";

    public static final String FFC_WIKI_ONLY_NAMES =
    "Aegon-Frey-(son-of-Stevron),Aegon-Frey-(son-of-Aenys),Aegor-Rivers,Aemon-Estermont,Aemon-Rivers,Aenys-Frey,Aggo,Agrivane,Alan,Alayaya,Albett,Alekyne-Florent,Alesander-Frey,Alesander-Staedmon,Alester-Norcross,Allard-Seaworth,Alyce,Alyce-Graceford,Alyn-Estermont,Alyn-Frey,Alyn-Haigh,Alys-Karstark,Alysane-Mormont,Alysanne-Hightower,Alysanne-Lefford,Alys-Frey,Alyssa-Blackwood,Alyx-Frey,Amarei-Crakehall,Andar-Royce,Andrew-Estermont,Androw-Frey,Annara-Farring,Antario-Jast,Arnolf-Karstark,Arron,Arron-Qorgyle,Arryk-Cargyll,Arthur-Ambrose,Arwyn-Frey,Arwyn-Oakheart,Assadora-of-Ibben,Axell-Florent,Azor-Ahai,Bandy,Barbrey-Dustin,Bearded-Ben,Beardless-Dick,Bedwyck,Bella,Bellegere-Otherys-(Courtesan),Bellena-Hawick,Belwas,Ben-Blackthumb,Ben-Plumm,Benedict-Broom,Benfred-Tallhart,Benfrey-Frey,Benjen-Stark,Bennard-Brune,Beren-Tallhart,Berena-Hornwood,Beth-Cassel,Bethany-Bolton,Bethany-Bracken,Bethany-Rosby,Betharios-of-Braavos,Big-Boil,Black-Bernarr,Black-Jack-Bulwer,Bluetooth,Bodger,Bowen-Marsh,Boy,Bradamar-Frey,Brandon-Norrey,Bran-the-Builder,Brandon-Tallhart,Branston-Cuy,Bryan-Frey,Bryen-Caron,Bryen-Farring,Burton-Crakehall,Carellen-Smallwood,Carolei-Waynwood,Cass,Castos,Cerenna-Lannister,Cersei-Frey,Cetheres,Clarence-Crabb-(Short),Cleon,Cley-Cerwyn,Clubfoot-Karl,Coldhands,Colin-Florent,Colmar-Frey,Conwy,Crawn,Cuger,Cynthea-Frey,Daario-Naharis,Dacey-Mormont,Daegon-Shepherd,Daeron-Targaryen-(son-of-Maekar-I),Dafyn-Vance,Dagos-Manwoody,Dake-(Guard),Dalbridge,Dale-Seaworth,Damon-Paege,Damon-Vypren,Dan,Dancy,Daughter-of-the-Dusk,Davos-Seaworth,Deana-Hardyng,Del,Delena-Florent,Della-Frey,Delonne-Allyrion,Delp,Dennis-Plumm,Denyo-Terys,Denys-Mallister,Denys-Redwyne,Denyse-Hightower,Desmond-Redwyne,Devan-Seaworth,Dickon-Frey,Dickon-Manwoody,Dirk,Domeric-Bolton,Donnel-Hill,Donnel-Locke,Dormund,Dryn,Duncan-Targaryen,Duram-Bar-Emmon,Dyah,Dywen,Ebben,Eddara-Tallhart,Eddard-Karstark,Edric-Dayne,Edric-Storm,Elaena-Targaryen,Elder-Brother,Eleanor-Mooton,Eleyna-Westerling,Elia-Sand,Ellery-Vance,Elmar-Frey,Elron,Elwood,Elwood-Meadows,Elyana-Vypren,Elyn-Norridge,Emberlei-Frey,Emphyria-Vance,Emrick,Erena-Glover,Erren-Florent,Errok,Erryk-Cargyll,Ferny,Ferrego-Antaryon,Galbart-Glover,Gallard,Gareth-Clifton,Garin-(Orphans),Garin-(Prince),Garizon,Garrett-Flowers,Garse-Flowers,Garse-Goodbrook,Garth-of-Greenaway,Garth-Greenfield,Garth-Greyfeather,Gawen-Glover,Gerald-Gower,Ghost-of-High-Heart,Gilbert-Farring,Glendon-Hewett,Goady,Goodwin,Grazdan-mo-Eraz,Greenbeard,Grey-Worm,Griffin-King,Grigg,Groleo,Grubbs,Gueren,Gulian-Qorgyle,Guyne,Gwayne-Corbray,Gyles,Halder,Hallis-Mollen,Halmon-Paege,Halys-Hornwood,Hareth-(Mole's-Town),Harodon,Harrion-Karstark,Harwood-Stout,Helly,Helman-Tallhart,Helya,Henk,High-Septon-(Aegons_Conquest),High-Septon-(fat_one),Hop-Robin,Hosman-Norcross,Hosteen-Frey,Hoster-Frey,Hother-Umber,Howland-Reed,Hugo-Wull,Husband,Igon-Vyrwel,Illyrio-Mopatis,Imry-Florent,Iron-Emmett,Irri,Jacelyn-Bywater,Jack-Be-Lucky,Jacks,Jaime-Frey,Jammos-Frey,Janei-Lannister,Jaqen-H'ghar,Jared-Frey,Jarl,Jasper-Waynwood,Jeren,Jeyne-Beesbury,Jeyne-Goodbrook,Jeyne-Lydden,Jeyne-Rivers,Jhaqo,Jhiqui,Jhogo,Joanna-Swyft,Jojen-Reed,Jommo,Jon-Brax,Jon-Bulwer,Jon-Cupps,Jon-O'Nutten,Jon-Umber-(Greatjon),Jon-Umber-(Smalljon),Jon-Vance,Jon-Waters,Jon-Wylde,Jonella-Cerwyn,Jonos-Bracken,Jonos-Frey,Jorah-Mormont,Jorelle-Mormont,Joss-Stilwood,Joss-the-Gloom,Joyeuse-Erenford,Jurne,Jyanna-Frey,Jynessa-Blackmont,Jyzene,Kegs,Kirth-Vance,Kyra,Kyra-Frey,Lady-of-the-Leaves,Lanna-Lannister,Larence-Snow,Larra-Blackmont,Lenn,Lenyl,Leo-Blackbar,Leo-Lefford,Leo-Tyrell-(son-of-Victor),Leobald-Tallhart,Leona-Tyrell,Leona-Woolfield,Leonette-Fossoway,Leslyn-Haigh,Lester-Morrigen,Lewys-the-Fishwife,Lewys-Lydden,Leyla-Hightower,Lia-Serry,Liane-Vance,Likely-Luke,Lomas-Estermont,Lomys,Lorcas,Lorent-Tyrell,Lothar-Frey,Lucas-Blackwood,Lucas-Tyrell,Luceon-Frey,Lucias-Vypren,Lucion-Lannister,Lucos-Chyttering,Luthor-Tyrell-(son-of-Moryn),Luthor-Tyrell-(son-of-Theodore),Lyanna-Mormont,Lyessa-Flint,Lymond-Goodbrook,Lymond-Lychester,Lymond-Vikary,Lynesse-Hightower,Lyonel-Frey,Lyonel-Tyrell-(son-of-Leo),Lyra-Mormont,Lysa-Meadows,Lythene-Frey,Mad-Huntsman,Maege-Mormont,Maegelle-Frey,Maekar-I-Targaryen,Mag-Mar-Tun-Doh-Weg,Mago,Malwyn-Frey,Marei,Margot-Lannister,Marianne-Vance,Maric-Seaworth,Marissa-Frey,Marlon-Manderly,Marq-Piper,Marsella-Waynwood,Martyn-Mullendore,Martyn-Rivers,Marya-Seaworth,Mathis-Frey,Matthar,Matthos-Seaworth,Mawney,Maynard,Medwick-Tyrell,Meera-Reed,Meg,Melara-Crane,Melesa-Crakehall,Melessa-Florent,Mellara-Rivers,Melwyn-Sarsfield,Melwys-Rivers,Merianne-Frey,Merlon-Crakehall,Merrell-Florent,Merrit,Mina-Tyrell,Mirri-Maz-Duur,Missandei,Monster,Monterys-Velaryon,Moro,Mors-Manwoody,Mors-Martell-(brother-of-Doran),Mors-Umber,Morton-Waynwood,Morya-Frey,Mudge-(brotherhood),Munda,Muttering-Bill,Mylenda-Caron,Myles-Mooton,Myrielle-Lannister,Nella,Nightingale,Normund-Tyrell,Norren,Notch,Nymella-Toland,Nymos,Ocley,Old-Tattersalt,Olene-Tyrell,Ollo-Lophand,Olymer-Tyrell,Olyvar-Frey,Omer-Blackberry,Omer-Florent,Ondrew-Locke,Orell,Orland-of-Oldtown,Oro-Tendyris,Orphan-Oss,Osha,Osmund-Frey,Othell-Yarwyck,Ottomore,Owen-(brother-of-Meribald),Owen-Norrey,Palla,Patchface,Pate-(King's-Landing),Patrek-Vance,Perra-Frey,Perriane-Frey,Perros-Blackmont,Peter-Plumm,Petyr-Frey,Polliver,Pono,Pyat-Pree,Pylos,Qarro-Volentin,Quaithe,Quenten-Banefort,Quentin-Tyrell,Quentyn-Qorgyle,Ragwyle,Rakharo,Rattleshirt,Ravella-Swann,Raymund-Frey,Raymund-Tyrell,Reek,Regenard-Estren,Renly-Norcross,Rhaegar-Frey,Rhaego,Rhaelle-Targaryen,Rhaena-Targaryen-(daughter-of-Aegon-III),Rhea-Florent,Rhialta-Vance,Rhogoro,Rhonda-Rowan,Rickard-Karstark,Rickard-Ryswell,Rickard-Tyrell,Rickard-Wylde,Robert-Brax-(son-of-Flement),Robert-Frey,Robert-Frey-(son-of-Raymund),Robin-Flint,Robin-Ryger,Rodrik-Cassel,Rodrik-Ryswell,Roger-Ryswell,Rollam-Westerling,Rolland-Storm,Rolph-Spicer,Ronel-Rivers,Roone-(maester),Roose-Ryswell,Rossart,Ryam-Florent,Rycherd-Crane,Ryella-Frey,Ryella-Royce,Ryger-Rivers,Ryk,Rylene-Florent,Ryon-Allyrion,S'vrone,Sailor's-Wife,Salladhor-Saan,Sallei-Paege,Samwell-Spicer,Sandor-Frey,Sarra-Frey,Sarya-Whent,Sebaston-Farman,Selmond-Stackspear,Selyse-Florent,Serra-Frey,Shadrick,Shagga,Sharna,Shella-Whent,Shiera-Crakehall,Shierle-Swyft,Shireen-Baratheon,Shirei-Frey,Shyra,Simon-Toyne,Skyte,Spare-Boot,Stafford-Lannister,Stannis-Seaworth,Steffon-Frey,Steffon-Seaworth,Steffon-Stackspear,Stevron-Frey,Stonesnake,Styr,Sybelle-Glover,Sylwa-Paege,Symon-Santagar,Symond-Frey,Tansy-(orphan),Tanton-Fossoway,Theodore-Tyrell,Theomar-Smallwood,Theomore,Three-Toes,Tim-Stone,Tim-Tangletongue,Tion-Frey,Titus-Peake,Tobho-Mott,Toefinger,Togg-Joth,Tommen-Costayne,Toregg,Tormund,Torren-Liddle,Torrhen-Karstark,Torwynd,Trebor-Jordayne,Tregar-Ormollen,Tremond-Gargalen,Triston-of-Tally-Hill,Triston-Farwynd,Turnip,Tyana-Wylde,Tybolt-Crakehall,Tybolt-Hetherspoon,Tygett-Lannister,Tyrion-Tanner,Tysane-Frey,Tyta-Frey,Tytos-Brax,Tywin-Frey-(son-of-Raymund),Ulwyck-Uller,Utherydes-Wayn,Victaria-Tyrell,Victor-Tyrell,Viserys-Targaryen,Vortimer-Crane,Vylarr,Walda-Frey-(daughter-of-Edwyn),Walda-Frey-(daughter-of-Lothar),Walda-Frey-(daughter-of-Walton),Walda-Frey-(daughter-of-Merrett),Walda-Frey-(daughter-of-Rhaegar),Walda-Rivers,Walder-Brax,Walder-Frey-(son-of-Jammos),Walder-Frey-(son-of-Merrett),Walder-Frey-(son-of-Emmon),Walder-Goodbrook,Walder-Haigh,Walder-Vance,Walton-Frey,Walton,Waltyr-Frey,Warryn-Beesbury,Wat-(orphan),Watty,Weeper,Wendel-Frey,Wendel-Manderly,Wex-Pyke,Whalen-Frey,Willamen-Frey,Willam-Dustin,Willem-Frey,Willem-Lannister,William-Mooton,Willifer,Willis-Wode,Wylla-Manderly,Wynafrei-Whent,Wynafryd-Manderly,Wynton-Stout,Xaro-Xhoan-Daxos,Ygritte,Zachery-Frey,Zarabelo,Zhoe-Blanetree,Zia-Frey";


    public static final String SOS_WIKI_ONLY_NAMES =
            "Aegon-Blackfyre,Aegon-Frey-(son-of-Aenys),Aemon-Blackfyre,Aemon-Estermont,Aeron-Greyjoy,Albar-Royce,Alekyne-Florent,Alesander-Frey,Alesander-Staedmon,Alester-Norcross,Allard-Seaworth,Alyn-Estermont,Alyn-Frey,Alyn-Haigh,Alyn-Stackspear,Alys-Frey,Alyssa-Blackwood,Amarei-Crakehall,Amerei-Frey,Andar-Royce,Andrik,Andros-Brax,Androw-Frey,Annara-Farring,Antario-Jast,Anvil-Ryn,Anya-Waynwood,Areo-Hotah,Arianne-Martell,Arwood-Frey,Arwyn-Frey,Arwyn-Oakheart,Aurane-Waters,Baelor-Blacktyde,Balman-Byrch,Beardless-Dick,Belgrave,Bellena-Hawick,Ben-(Big-Belly),Benedict-Broom,Benfred-Tallhart,Beren-Tallhart,Berena-Hornwood,Beth-Cassel,Bethany-Bracken,Bethany-Rosby,Betharios-of-Braavos,Black-Jack-Bulwer,Bluetooth,Bonifer-Hasty,Bradamar-Frey,Brandon-Tallhart,Bryan-Frey,Bryen-Farring,Caleotte,Carolei-Waynwood,Cassana-Estermont,Cerenna-Lannister,Cersei-Frey,Clement-Piper,Colin-Florent,Colmar-Frey,Conwy,Cuger,Cynthea-Frey,Dafyn-Vance,Dalbridge,Dale-Seaworth,Damion-Lannister,Damon-Paege,Damon-Vypren,Danos-Slynt,Darlessa-Marbrand,Deana-Hardyng,Delena-Florent,Della-Frey,Delonne-Allyrion,Dennis-Plumm,Dermot,Devan-Seaworth,Dickon-Frey,Dryn,Dunstan-Drumm,Eddara-Tallhart,Edmyn-Tully,Elaena-Targaryen,Eldon-Estermont,Elia-Sand,Ellery-Vance,Elron,Elwood-Meadows,Elyana-Vypren,Elyn-Norridge,Emberlei-Frey,Erena-Glover,Ermesande-Hayford,Erren-Florent,Falyse-Stokeworth,Garrett-Flowers,Garse-Flowers,Garth-Greenfield,Garth-Tyrell,Gawen-Glover,Gerion-Lannister,Gevin-Harlaw,Ghost-of-High-Heart,Gilwood-Hunter,Gormon-Tyrell,Gorold-Goodbrother,Grazdan-(Great),Gueren,Gulian-Swann,Gylbert-Farwynd,Hallyne,Halmon-Paege,Hareth-(Mole's-Town),Harwyn-Plumm,Helya,High-Septon-(fat_one),Horas-Redwyne,Hosman-Norcross,Hoster-Frey,Hother-Umber,Hugo-Vance,Igon-Vyrwel,Iron-Emmett,Ironbelly,Jacks,Jammos-Frey,Janei-Lannister,Jaqen-H'ghar,Jared-Frey,Jarmen-Buckwell,Jeren,Jeyne-Beesbury,Jeyne-Darry,Jeyne-Goodbrook,Jeyne-Lydden,Jeyne-Rivers,Jeyne-Westerling-(wife-of-Maegor-I),Jhaqo,Joanna-Swyft,Jon-Brax,Jon-Bulwer,Jon-Myre,Jon-Wylde,Jonella-Cerwyn,Jonos-Frey,Josmyn-Peckledon,Joy-Hill,Joyeuse-Erenford,Jurne,Jyanna-Frey,Karyl-Vance,Kirth-Vance,Kyle-(brotherhood),Kym,Kyra-Frey,Lanna-Lannister,Larence-Snow,Leo-Blackbar,Leo-Lefford,Leo-Tyrell,Leo-Tyrell-(son-of-Victor),Leona-Tyrell,Lewys-Lydden,Lia-Serry,Lomas-Estermont,Lomys,Lorent-Tyrell,Loreza-Sand,Lucas-Tyrell,Luceon-Frey,Lucion-Lannister,Luthor-Tyrell-(son-of-Moryn),Luthor-Tyrell-(son-of-Theodore),Lyessa-Flint,Lyle-Crakehall,Lymond-Vikary,Lyonel-Frey,Lyonel-Tyrell-(son-of-Leo),Lysa-Meadows,Lythene-Frey,Maegelle-Frey,Maggy,Malwyn-Frey,Margot-Lannister,Marianne-Vance,Maric-Seaworth,Marissa-Frey,Mariya-Darry,Mark-Mullendore,Maron-Botley,Maron-Greyjoy,Maron-Volmark,Marsella-Waynwood,Marwyn-Belmore,Mathis-Frey,Matthar,Matthos-Seaworth,Medwick-Tyrell,Melara-Crane,Meldred-Merlyn,Melesa-Crakehall,Melessa-Florent,Mellara-Rivers,Melwyn-Sarsfield,Melwys-Rivers,Merianne-Frey,Merrell-Florent,Mina-Tyrell,Minisa-Whent,Monster,Monterys-Velaryon,Mors-Umber,Morton-Waynwood,Morya-Frey,Moryn-Tyrell,Mudge-(brotherhood),Mya-Stone,Mychel-Redfort,Mylenda-Caron,Myranda-Royce,Myrielle-Lannister,Norbert-Vance,Normund-Tyrell,Norne-Goodbrother,Nymella-Toland,Nymeria-Sand,Obara-Sand,Obella-Sand,Olene-Tyrell,Olymer-Tyrell,Omer-Florent,Ondrew-Locke,Oro-Tendyris,Orton-Merryweather,Osmund-Frey,Ossifer-Plumm,Otter-Gimpknee,Pate-(Shermer's-Grove),Patrek-Vance,Penny,Perra-Frey,Perriane-Frey,Perros-Blackmont,Peter-Plumm,Philip-Plumm,Pono,Qarl-the-Maid,Lord-Commander-Qorgyle,Quenten-Banefort,Quentin-Tyrell,Raymund-Tyrell,Regenard-Estren,Renly-Norcross,Rhaegar-Frey,Rhea-Florent,Rickard-Tyrell,Rickard-Wylde,Robert-Brax,Robert-Brax-(son-of-Flement),Robert-Frey,Robert-Frey-(son-of-Raymund),Robert-Paege,Rodrik-Greyjoy,Rodrik-Harlaw,Rolley,Ronel-Rivers,Ronnet-Connington,Roone-(maester),Rugen,Rupert-Brax,Ryam-Florent,Rycherd-Crane,Ryella-Frey,Ryella-Royce,Rylene-Florent,Sallei-Paege,Sandor-Frey,Sarella-Sand,Sarra-Frey,Sarya-Whent,Sawane-Botley,Selmond-Stackspear,Serra-Frey,Shella-Whent,Shiera-Crakehall,Shierle-Swyft,Shirei-Frey,Sigrin,Skyte,Stafford-Lannister,Stannis-Seaworth,Steffon-Frey,Steffon-Seaworth,Steffon-Stackspear,Steffon-Swyft,Stygg,Sybelle-Glover,Sylwa-Paege,Symond-Frey,Tanton-Fossoway,Ternesio-Terys,Theodore-Tyrell,Theomar-Smallwood,Timon,Titus-Peake,Togg-Joth,Tomard,Tristan-Ryger,Trystane-Martell,Tyana-Wylde,Tybolt-Crakehall,Tyene-Sand,Tygett-Lannister,Tyrek-Lannister,Tysane-Frey,Tyta-Frey,Tytos-Frey,Tywin-Frey,Urzen,Vickon-Greyjoy,Victaria-Tyrell,Victarion-Greyjoy,Victor-Tyrell,Visenya-Targaryen,Viserys-II-Targaryen,Vortimer-Crane,Vylarr,Walda-Frey-(daughter-of-Edwyn),Walda-Frey-(daughter-of-Lothar),Walda-Frey-(daughter-of-Walton),Walda-Frey-(daughter-of-Rhaegar),Walda-Rivers,Walda-Rivers-(daughter-of-Aemon),Walder-Brax,Walder-Frey-(son-of-Jammos),Walder-Frey-(son-of-Ryman),Walder-Frey-(son-of-Merrett),Walder-Frey-(son-of-Emmon),Walder-Goodbrook,Walder-Haigh,Walder-Vance,Waldon-Wynch,Walton-Frey,Waltyr-Frey,Wat-(Night's-Watch),Wat-(Whitesmile),Wendamyr,Wendel-Frey,Wex-Pyke,Willamen-Frey,Willem-Frey,William-Mooton,Wynafrei-Whent,Zachery-Frey,Zhoe-Blanetree,Zia-Frey,Id,Aegon-Blackfyre,Aegon-Frey-(son-of-Aenys),Aemon-Blackfyre,Aemon-Estermont,Aeron-Greyjoy,Albar-Royce,Alekyne-Florent,Alesander-Frey,Alesander-Staedmon,Alester-Norcross,Allard-Seaworth,Alyn-Estermont,Alyn-Frey,Alyn-Haigh,Alyn-Stackspear,Alys-Frey,Alyssa-Blackwood,Amarei-Crakehall,Amerei-Frey,Andar-Royce,Andrik,Andros-Brax,Androw-Frey,Annara-Farring,Antario-Jast,Anvil-Ryn,Anya-Waynwood,Areo-Hotah,Arianne-Martell,Arwood-Frey,Arwyn-Frey,Arwyn-Oakheart,Aurane-Waters,Baelor-Blacktyde,Balman-Byrch,Beardless-Dick,Belgrave,Bellena-Hawick,Ben-(Big-Belly),Benedict-Broom,Benfred-Tallhart,Beren-Tallhart,Berena-Hornwood,Beth-Cassel,Bethany-Bracken,Bethany-Rosby,Betharios-of-Braavos,Black-Jack-Bulwer,Bluetooth,Bonifer-Hasty,Bradamar-Frey,Brandon-Tallhart,Bryan-Frey,Bryen-Farring,Caleotte,Carolei-Waynwood,Cassana-Estermont,Cerenna-Lannister,Cersei-Frey,Clement-Piper,Colin-Florent,Colmar-Frey,Conwy,Cuger,Cynthea-Frey,Dafyn-Vance,Dalbridge,Dale-Seaworth,Damion-Lannister,Damon-Paege,Damon-Vypren,Danos-Slynt,Darlessa-Marbrand,Deana-Hardyng,Delena-Florent,Della-Frey,Delonne-Allyrion,Dennis-Plumm,Dermot,Devan-Seaworth,Dickon-Frey,Dryn,Dunstan-Drumm,Eddara-Tallhart,Edmyn-Tully,Elaena-Targaryen,Eldon-Estermont,Elia-Sand,Ellery-Vance,Elron,Elwood-Meadows,Elyana-Vypren,Elyn-Norridge,Emberlei-Frey,Erena-Glover,Ermesande-Hayford,Erren-Florent,Falyse-Stokeworth,Garrett-Flowers,Garse-Flowers,Garth-Greenfield,Garth-Tyrell,Gawen-Glover,Gerion-Lannister,Gevin-Harlaw,Ghost-of-High-Heart,Gilwood-Hunter,Gormon-Tyrell,Gorold-Goodbrother,Grazdan-(Great),Gueren,Gulian-Swann,Gylbert-Farwynd,Hallyne,Halmon-Paege,Hareth-(Mole's-Town),Harwyn-Plumm,Helya,High-Septon-(fat_one),Horas-Redwyne,Hosman-Norcross,Hoster-Frey,Hother-Umber,Hugo-Vance,Igon-Vyrwel,Iron-Emmett,Ironbelly,Jacks,Jammos-Frey,Janei-Lannister,Jaqen-H'ghar,Jared-Frey,Jarmen-Buckwell,Jeren,Jeyne-Beesbury,Jeyne-Darry,Jeyne-Goodbrook,Jeyne-Lydden,Jeyne-Rivers,Jeyne-Westerling-(wife-of-Maegor-I),Jhaqo,Joanna-Swyft,Jon-Brax,Jon-Bulwer,Jon-Myre,Jon-Wylde,Jonella-Cerwyn,Jonos-Frey,Josmyn-Peckledon,Joy-Hill,Joyeuse-Erenford,Jurne,Jyanna-Frey,Karyl-Vance,Kirth-Vance,Kyle-(brotherhood),Kym,Kyra-Frey,Lanna-Lannister,Larence-Snow,Leo-Blackbar,Leo-Lefford,Leo-Tyrell,Leo-Tyrell-(son-of-Victor),Leona-Tyrell,Lewys-Lydden,Lia-Serry,Lomas-Estermont,Lomys,Lorent-Tyrell,Loreza-Sand,Lucas-Tyrell,Luceon-Frey,Lucion-Lannister,Luthor-Tyrell-(son-of-Moryn),Luthor-Tyrell-(son-of-Theodore),Lyessa-Flint,Lyle-Crakehall,Lymond-Vikary,Lyonel-Frey,Lyonel-Tyrell-(son-of-Leo),Lysa-Meadows,Lythene-Frey,Maegelle-Frey,Maggy,Malwyn-Frey,Margot-Lannister,Marianne-Vance,Maric-Seaworth,Marissa-Frey,Mariya-Darry,Mark-Mullendore,Maron-Botley,Maron-Greyjoy,Maron-Volmark,Marsella-Waynwood,Marwyn-Belmore,Mathis-Frey,Matthar,Matthos-Seaworth,Medwick-Tyrell,Melara-Crane,Meldred-Merlyn,Melesa-Crakehall,Melessa-Florent,Mellara-Rivers,Melwyn-Sarsfield,Melwys-Rivers,Merianne-Frey,Merrell-Florent,Mina-Tyrell,Minisa-Whent,Monster,Monterys-Velaryon,Mors-Umber,Morton-Waynwood,Morya-Frey,Moryn-Tyrell,Mudge-(brotherhood),Mya-Stone,Mychel-Redfort,Mylenda-Caron,Myranda-Royce,Myrielle-Lannister,Norbert-Vance,Normund-Tyrell,Norne-Goodbrother,Nymella-Toland,Nymeria-Sand,Obara-Sand,Obella-Sand,Olene-Tyrell,Olymer-Tyrell,Omer-Florent,Ondrew-Locke,Oro-Tendyris,Orton-Merryweather,Osmund-Frey,Ossifer-Plumm,Otter-Gimpknee,Pate-(Shermer's-Grove),Patrek-Vance,Penny,Perra-Frey,Perriane-Frey,Perros-Blackmont,Peter-Plumm,Philip-Plumm,Pono,Qarl-the-Maid,Lord-Commander-Qorgyle,Quenten-Banefort,Quentin-Tyrell,Raymund-Tyrell,Regenard-Estren,Renly-Norcross,Rhaegar-Frey,Rhea-Florent,Rickard-Tyrell,Rickard-Wylde,Robert-Brax,Robert-Brax-(son-of-Flement),Robert-Frey,Robert-Frey-(son-of-Raymund),Robert-Paege,Rodrik-Greyjoy,Rodrik-Harlaw,Rolley,Ronel-Rivers,Ronnet-Connington,Roone-(maester),Rugen,Rupert-Brax,Ryam-Florent,Rycherd-Crane,Ryella-Frey,Ryella-Royce,Rylene-Florent,Sallei-Paege,Sandor-Frey,Sarella-Sand,Sarra-Frey,Sarya-Whent,Sawane-Botley,Selmond-Stackspear,Serra-Frey,Shella-Whent,Shiera-Crakehall,Shierle-Swyft,Shirei-Frey,Sigrin,Skyte,Stafford-Lannister,Stannis-Seaworth,Steffon-Frey,Steffon-Seaworth,Steffon-Stackspear,Steffon-Swyft,Stygg,Sybelle-Glover,Sylwa-Paege,Symond-Frey,Tanton-Fossoway,Ternesio-Terys,Theodore-Tyrell,Theomar-Smallwood,Timon,Titus-Peake,Togg-Joth,Tomard,Tristan-Ryger,Trystane-Martell,Tyana-Wylde,Tybolt-Crakehall,Tyene-Sand,Tygett-Lannister,Tyrek-Lannister,Tysane-Frey,Tyta-Frey,Tytos-Frey,Tywin-Frey,Urzen,Vickon-Greyjoy,Victaria-Tyrell,Victarion-Greyjoy,Victor-Tyrell,Visenya-Targaryen,Viserys-II-Targaryen,Vortimer-Crane,Vylarr,Walda-Frey-(daughter-of-Edwyn),Walda-Frey-(daughter-of-Lothar),Walda-Frey-(daughter-of-Walton),Walda-Frey-(daughter-of-Rhaegar),Walda-Rivers,Walda-Rivers-(daughter-of-Aemon),Walder-Brax,Walder-Frey-(son-of-Jammos),Walder-Frey-(son-of-Ryman),Walder-Frey-(son-of-Merrett),Walder-Frey-(son-of-Emmon),Walder-Goodbrook,Walder-Haigh,Walder-Vance,Waldon-Wynch,Walton-Frey,Waltyr-Frey,Wat-(Night's-Watch),Wat-(Whitesmile),Wendamyr,Wendel-Frey,Wex-Pyke,Willamen-Frey,Willem-Frey,William-Mooton,Wynafrei-Whent,Zachery-Frey,Zhoe-Blanetree,Zia-Frey";


    public static void main(String[] args) {

        /*
         Clash of Kings
         */

//        processFiles("src/main/resources/text/clashofkings.txt",
//                "src/main/resources/data/characters/mynotoar/GameOfThronesAppendixCatalog.csv",
//                "src/main/resources/data/characters/mynotoar/COK-GameOfThronesAppendix-Keep.csv",
//                "src/main/resources/data/characters/mynotoar/COK-GameOfThronesAppendix-Remove.csv"
//                );
//
//        processFiles("src/main/resources/text/clashofkings.txt",
//                "src/main/resources/data/characters/mynotoar/GameOfThronesCatalog2.csv",
//                "src/main/resources/data/characters/mynotoar/COK-GameOfThrones-Keep.csv",
//                "src/main/resources/data/characters/mynotoar/COK-GameOfThrones-Remove.csv"
//        );

//        processList("src/main/resources/text/clashofkings.txt", COK_WIKI_ONLY_NAMES);


        /*
        Storm of Swords

        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/mynotoar/GameOfThronesCatalog.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThrones-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThrones-Remove.csv"
        );

        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/mynotoar/GameOfThronesAppendixCatalog.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThronesAppendix-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThronesAppendix-Remove.csv"
                );

        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/mynotoar/ClashOfKingsCatalog.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKings-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKings-Remove.csv"
        );

        processFiles("src/main/resources/text/stormofswords.txt",
                "src/main/resources/data/characters/mynotoar/ClashOfKingsAppendixCatalog.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKingsAppendix-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKingsAppendix-Remove.csv"
        );

*/


        /// createAddFile();

        String[] sosCatalogFileNames = new String[] {
                "src/main/resources/data/characters/mynotoar/GameOfThronesCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/GameOfThronesAppendixCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/ClashOfKingsCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/ClashOfKingsAppendixCatalogShort.csv"
        };


//        updateCharListFile("src/main/resources/data/characters/sos-list-merged-clean.csv",
//                "soswiki",
//                new String[] {"src/main/resources/data/characters/sos-list-wiki-only-short.csv"},
//                "src/main/resources/text/stormofswords.txt");

//        updateCharListFile("src/main/resources/data/characters/sos-list-curated-hyphenated.csv",
//                sosCatalogFileNames,
//                "src/main/resources/text/stormofswords.txt");



        String[] ffcCatalogFileNames = new String[] {
                "src/main/resources/data/characters/mynotoar/GameOfThronesCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/GameOfThronesAppendixCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/ClashOfKingsCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/ClashOfKingsAppendixCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/StormOfSwordsCatalogShort.csv",
                "src/main/resources/data/characters/mynotoar/StormOfSwordsAppendixCatalogShort.csv"
        };



//        updateCharListFile("src/main/resources/data/characters/ffc-list-merge-temp3.csv",
//                "ffcmyno",
//                ffcCatalogFileNames,
//                "src/main/resources/text/feastforcrows.txt");

        processList("src/main/resources/text/stormofswords.txt", SOS_WIKI_ONLY_NAMES);



    }




    private static void updateCharListFile(String charFileName, String suffix, String[] charCatalogNames, String textFileName) {

        Map<String, String> charMap = new TreeMap<String, String>();

        List<String> charList = FileUtils.readFile(charFileName);

        for (String currentLine : charList) {
            String currentKey = currentLine.split(",")[0];
            charMap.put(currentKey, "old," + currentLine);
        }


        Map<String, String> tempMap;

        for (int i=0; i < charCatalogNames.length; i++) {
            tempMap = processFile(textFileName, charCatalogNames[i]);
            updateCharacterMap(charMap, tempMap);
        }

        List<String> updatedList = new ArrayList<String>();

        for (String k : charMap.keySet()) {
            updatedList.add(charMap.get(k));
        }

        System.out.println("updatedList=" + updatedList.size());



        for (String temp : updatedList) {
            //System.out.println(temp);

            if (temp == null) {
                System.out.println("\t\t========" + temp);
            }
        }

        String filePrefix = charFileName.split("\\.")[0];
        FileUtils.writeFile(updatedList, filePrefix + "-" +suffix +".csv");


    }

    /**
     * Goes through the original text and then makes a recommendation for each character in the given catelog: keep or remove.
     * @param textName
     * @param catalogName
     */
    private static Map<String,String> processFile(String textName, String catalogName) {
        List<String> charLines = FileUtils.readFile(catalogName);
        HashMap<String, String[]> charMap = new HashMap<String, String[]>();
        HashMap<String, String> charLineMap = new HashMap<String, String>();

        System.out.println("Processing book:" + textName + " and catalog:" + catalogName);


        for (String line : charLines) {
            String[] tokens = line.split(",");
            String key = tokens[1].replace("\"","");
            charMap.put(key, tokens);
            charLineMap.put(key,line);
        }

        List<String> textLines = FileUtils.readFile(textName);

        for (String textLine : textLines) {

            List<String> removalList = new ArrayList<String>();

            for (String key : charMap.keySet()) {
                String[] tokens = charMap.get(key);

                if (textLine.contains(tokens[SHORT_CAT_NAME_INDEX])) {
                    // name
                    System.out.println("matched: " + key + " name token:" + tokens[SHORT_CAT_NAME_INDEX]);
                    removalList.add(key);
                } else if ( tokens.length > SHORT_CAT_ALIAS_INDEX &&
                        tokens[SHORT_CAT_ALIAS_INDEX].length() > 0 && textLine.contains(tokens[SHORT_CAT_ALIAS_INDEX])) {
                    System.out.println("matched: " + key + " alias token:" + tokens[SHORT_CAT_ALIAS_INDEX]);
                    removalList.add(key);
                } else if ( tokens.length > SHORT_CAT_SURNAME_INDEX &&
                        tokens[SHORT_CAT_TITLE_INDEX].length() > 0) {
                    if (tokens[SHORT_CAT_SURNAME_INDEX].length() >0) {
                        if (textLine.contains(tokens[SHORT_CAT_TITLE_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX])) {
                            System.out.println("matched: " + key + " title/last name token:" + tokens[SHORT_CAT_TITLE_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX]);
                            removalList.add(key);
                        }
                    }

                    if (tokens[SHORT_CAT_SURNAME_INDEX].length() >0) {
                        if (textLine.contains(tokens[SHORT_CAT_FORENAME_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX])) {
                            System.out.println("matched: " + key + " first/last name token:" + tokens[SHORT_CAT_FORENAME_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX]);
                            removalList.add(key);
                        }
                    }
                }


            }

            if (removalList.size() > 0) {
                for (String k : removalList) {
                    charMap.remove(k);
                }
            }
        }


        System.out.println("===== didn't match " + charMap.size());

        for (String key : charMap.keySet()) {
            System.out.println("\t suggest removing:" + key );
        }

        TreeMap<String, String> keepMap = new TreeMap<String, String>();


        List<String> keepLines = new ArrayList<String>();
        List<String> removeLines = new ArrayList<String>();

        for (String line : charLines) {
            String charKey = line.split(",")[SHORT_CAT_ID_INDEX];
            System.out.println("\t===charKey=[" + charKey + "]");

            if (charMap.containsKey(charKey)) {
                removeLines.add(line);
            } else {
                keepLines.add(line);
                keepMap.put(charKey,charLineMap.get(charKey));
            }
        }

        String filePrefix = catalogName.split("\\.")[0];

        FileUtils.writeFile(keepLines,filePrefix + "_SOSkeep.csv");
        FileUtils.writeFile(removeLines,filePrefix + "_SOSremove.csv");

        return keepMap;
    }




    /**
     * Goes through the original text and then makes a recommendation for each character in the given catelog: keep or remove.
     * @param textName
     * @param catalogName
     * @param keepCatalogName
     * @param removeCatalogName
     */
    private static Map<String,String> processFiles(String textName,
                                            String catalogName, String keepCatalogName, String removeCatalogName) {
        List<String> charLines = FileUtils.readFile(catalogName);
        HashMap<String, String[]> charMap = new HashMap<String, String[]>();
        HashMap<String, String> charLineMap = new HashMap<String, String>();

        System.out.println("Processing book:" + textName + " and catalog:" + catalogName);


        for (String line : charLines) {
            String[] tokens = line.split(",");
            String key = tokens[1].replace("\"","");
            charMap.put(key, tokens);
            charLineMap.put(key,line);
        }

        List<String> textLines = FileUtils.readFile(textName);

        for (String textLine : textLines) {

            List<String> removalList = new ArrayList<String>();

            for (String key : charMap.keySet()) {
                String[] tokens = charMap.get(key);

                if (textLine.contains(tokens[SHORT_CAT_NAME_INDEX])) {
                    // name
                    System.out.println("matched: " + key + " name token:" + tokens[SHORT_CAT_NAME_INDEX]);
                    removalList.add(key);
                } else if ( tokens.length > SHORT_CAT_ALIAS_INDEX &&
                        tokens[SHORT_CAT_ALIAS_INDEX].length() > 0 &&  hasAlias(textLine, tokens[SHORT_CAT_ALIAS_INDEX].split(";"))) {
                    System.out.println("matched: " + key + " alias token:" + tokens[SHORT_CAT_ALIAS_INDEX]);
                    removalList.add(key);
                } else if ( tokens.length > SHORT_CAT_SURNAME_INDEX &&
                        tokens[SHORT_CAT_TITLE_INDEX].length() > 0) {
                    if (tokens[SHORT_CAT_SURNAME_INDEX].length() >0) {
                        if (textLine.contains(tokens[SHORT_CAT_TITLE_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX])) {
                            System.out.println("matched: " + key + " title/last name token:" + tokens[SHORT_CAT_TITLE_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX]);
                            removalList.add(key);
                        }
                    }

                    if (tokens[SHORT_CAT_SURNAME_INDEX].length() >0) {
                        if (textLine.contains(tokens[SHORT_CAT_FORENAME_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX])) {
                            System.out.println("matched: " + key + " first/last name token:" + tokens[SHORT_CAT_FORENAME_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX]);
                            removalList.add(key);
                        }
                    }
                }


            }

            if (removalList.size() > 0) {
                for (String k : removalList) {
                    charMap.remove(k);
                }
            }
        }


        System.out.println("===== didn't match " + charMap.size());

        for (String key : charMap.keySet()) {
            System.out.println("\t suggest removing:" + key );
        }

       TreeMap<String, String> keepMap = new TreeMap<String, String>();


        List<String> keepLines = new ArrayList<String>();
        List<String> removeLines = new ArrayList<String>();

        for (String line : charLines) {
            String charKey = line.split(",")[SHORT_CAT_ID_INDEX];
            System.out.println("\t===charKey=[" + charKey + "]");

            if (charMap.containsKey(charKey)) {
                removeLines.add(line);
            } else {
                keepLines.add(line);
                keepMap.put(charKey,charLineMap.get(charKey));
            }
        }

        FileUtils.writeFile(keepLines,keepCatalogName);
        FileUtils.writeFile(removeLines,removeCatalogName);



        return keepMap;
    }


    private static boolean hasAlias(String line, String[] alias) {
        boolean retVal = false;

        for (String a : alias) {
            if (line.contains(a)) {
                retVal = true;
            }
        }

        return retVal;
    }



    /**
     * Goes through the original text and then makes a recommendation for each character in the given catelog: keep or remove.
     * @param textName
     * @param catalogName
     * @param keepCatalogName
     * @param removeCatalogName
     */
//    private Map<String,String> processFiles(String textName, String currentListName,
//                                     String catalogName, String keepCatalogName, String removeCatalogName) {
//        List<String> charLines = FileUtils.readFile(catalogName);
//        HashMap<String, String[]> charMap = new HashMap<String, String[]>();
//        HashMap<String, String> charLineMap = new HashMap<String, String>();
//
//        System.out.println("Processing book:" + textName + " and catalog:" + catalogName);
//
//
//        for (String line : charLines) {
//            String[] tokens = line.split(",");
//            String key = tokens[1].replace("\"","");
//            charMap.put(key, tokens);
//            charLineMap.put(key,line);
//        }
//
//        List<String> textLines = FileUtils.readFile(textName);
//
//        for (String textLine : textLines) {
//
//            List<String> removalList = new ArrayList<String>();
//
//            for (String key : charMap.keySet()) {
//                String[] tokens = charMap.get(key);
//
//                if (textLine.contains(tokens[SHORT_CAT_NAME_INDEX])) {
//                    // name
//                    System.out.println("matched: " + key + " name token:" + tokens[SHORT_CAT_NAME_INDEX]);
//                    removalList.add(key);
//                } else if ( tokens.length > SHORT_CAT_ALIAS_INDEX &&
//                        tokens[SHORT_CAT_ALIAS_INDEX].length() > 0 && textLine.contains(tokens[SHORT_CAT_ALIAS_INDEX])) {
//                    System.out.println("matched: " + key + " alias token:" + tokens[SHORT_CAT_ALIAS_INDEX]);
//                    removalList.add(key);
//                } else if ( tokens.length > SHORT_CAT_SURNAME_INDEX &&
//                        tokens[SHORT_CAT_TITLE_INDEX].length() > 0) {
//                    if (tokens[SHORT_CAT_SURNAME_INDEX].length() >0) {
//                        if (textLine.contains(tokens[SHORT_CAT_TITLE_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX])) {
//                            System.out.println("matched: " + key + " title/last name token:" + tokens[SHORT_CAT_TITLE_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX]);
//                            removalList.add(key);
//                        }
//                    }
//
//                    if (tokens[SHORT_CAT_SURNAME_INDEX].length() >0) {
//                        if (textLine.contains(tokens[SHORT_CAT_FORENAME_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX])) {
//                            System.out.println("matched: " + key + " first/last name token:" + tokens[SHORT_CAT_FORENAME_INDEX] + ' ' + tokens[SHORT_CAT_SURNAME_INDEX]);
//                            removalList.add(key);
//                        }
//                    }
//                }
//
//
//            }
//
//            if (removalList.size() > 0) {
//                for (String k : removalList) {
//                    charMap.remove(k);
//                }
//            }
//        }
//
//
//        System.out.println("===== didn't match " + charMap.size());
//
//        for (String key : charMap.keySet()) {
//            System.out.println("\t suggest removing:" + key );
//        }
//
//        List<String> currentCharList = FileUtils.readFile(currentListName);
//
//        TreeMap<String, String> currentMap = new TreeMap<String, String>();
//        TreeMap<String, String> keepMap = new TreeMap<String, String>();
//
//
//        for (String currentLine : currentCharList) {
//            String currentKey = currentLine.split(",")[0];
//            currentMap.put(currentKey, "old," + currentLine);
//        }
//
//        List<String> keepLines = new ArrayList<String>();
//        List<String> removeLines = new ArrayList<String>();
//
//        for (String line : charLines) {
//            String charKey = line.split(",")[SHORT_CAT_ID_INDEX];
//            System.out.println("\t===charKey=[" + charKey + "]");
//
//            if (charMap.containsKey(charKey)) {
//                removeLines.add(line);
//            } else {
//                keepLines.add(line);
//
//                if (! currentMap.containsKey(charKey)) {
//                    System.out.println("Adding for [" + charKey + "]");
//                    currentMap.put(charKey, charLineMap.get(charKey));
//                    keepMap.put(charKey,charLineMap.get(charKey));
//                }
//            }
//        }
//
//        FileUtils.writeFile(keepLines,keepCatalogName);
//        FileUtils.writeFile(removeLines,removeCatalogName);
//
//        List<String> updatedList = new ArrayList<String>();
//
//        for (String k : currentMap.keySet()) {
//            updatedList.add(currentMap.get(k));
//        }
//
//        System.out.println("updatedList=" + updatedList.size());
//
//
//
//        for (String temp : updatedList) {
//            //System.out.println(temp);
//
//            if (temp == null) {
//                System.out.println("\t\t========" + temp);
//            }
//         }
//
//       FileUtils.writeFile(updatedList, currentListName + "2");
//
//
//        return keepMap;
//    }



    /**
     * Adds key/value pairs in newCharMap to currentCharMap, provided that the key does not already exist.
     * @param currentCharMap
     * @param newCharMap
     * @return
     */
    private static void updateCharacterMap(Map<String,String> currentCharMap, Map<String,String> newCharMap) {

        for (String newKey : newCharMap.keySet()) {
            if (!currentCharMap.containsKey(newKey)) {
                System.out.println("Adding for [" + newKey + "]");
                currentCharMap.put(newKey, newCharMap.get(newKey));
            }
        }
    }



    /**
     * Creates a character list for the next book, marking some as "carryover" (from previous books)
     * and "keep" (first appearing in this book)
     */
    private static void createAddFile() {
        String[] previousListFiles = { "src/main/resources/data/characters/got-list-merged.csv",
                "src/main/resources/data/characters/cok-list-merged2.csv" };

        String[] keepFiles = {
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThrones-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-GameOfThronesAppendix-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKings-Keep.csv",
                "src/main/resources/data/characters/mynotoar/SOS-ClashOfKingsAppendix-Keep.csv",
        };

        Map<String, String> map = new HashMap<String, String>();


        for (int i=0; i < previousListFiles.length; i++) {
            List<String> lineList = FileUtils.readFile(previousListFiles[0]);

            for (String line : lineList) {
                map.put(line.split(",")[0], line);
            }
        }

        List<String> newLines = new ArrayList<String>();

        for (int j=0; j < keepFiles.length; j++) {
            List<String> keepLines = FileUtils.readFile(keepFiles[j]);

            for (String keep : keepLines) {
                String key = keep.split(",")[0];
                keep = (map.containsKey(key)) ? "carryover," + map.get(key) : "keep," +  keep;

                newLines.add(keep);
            }

        }

        FileUtils.writeFile(newLines,"src/main/resources/data/characters/mynotoar/SOS-keep.csv");

    }






    /**
     * Similar to processFiles, but this one takes a comma delimited string of names as an input.
     * This is useful at the end of the character list process, when there is a disparity between the potential lists.
     * This function makes a recommendation, but is not fool proof! It searches the text for the full character name
     * and the character first name.
     * @param textName
     * @param names
     */
    private static void processList(String textName, String names) {


        List<String> textLines = FileUtils.readFile(textName);

        List<String> nameList = new ArrayList<String>();

        String[] nameArray = names.split(",");

        for (String w : nameArray) {
            nameList.add(w.split("-\\(")[0]);
        }

        for (String textLine : textLines) {

            List<String> removalList = new ArrayList<String>();

            for (String id : nameList) {


                String  name = id.replace('-',' ');
                String firstName = name.split(" ")[0];

                if (textLine.contains(id)) {
                    System.out.println("\tmatched: " + id + " token:" + id);
                    removalList.add(id);
                } else if ( textLine.contains(name)) {
                    System.out.println("\tmatched: " + id + " token:" + name);
                    removalList.add(id);
                } else if (textLine.contains(firstName)) {
                    System.out.println("\tmatched: " + id + " token:" + firstName);
                    removalList.add(id);
                }


            }

            if (removalList.size() > 0) {
                for (String k : removalList) {
                    nameList.remove(k);
                }
            }
        }


        System.out.println("===== didn't match " + nameList.size());

        for (String key : nameList) {
            System.out.println("\t suggest removing: " + key );
        }


//        List<String> keepLines = new ArrayList<String>();
//        List<String> removeLines = new ArrayList<String>();
//
//        for (String line : charLines) {
//            String charKey = line.split(",")[0];
//
//            if (charMap.containsKey(charKey)) {
//                removeLines.add(line);
//            } else {
//                keepLines.add(line);
//            }
//        }
//
//        FileUtils.writeFile(keepLines,keepCatalogName);
//        FileUtils.writeFile(removeLines,removeCatalogName);

    }


}
