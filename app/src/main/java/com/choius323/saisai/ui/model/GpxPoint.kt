package com.choius323.saisai.ui.model

import com.kakao.vectormap.LatLng

data class GpxPoint(
    val lat: Double, // 위도
    val lng: Double, // 경도
    val elevation: Double,
    val segmentDistance: Double,
) {
    fun toLatLng() = LatLng.from(lat, lng)

    companion object {
        val gpxPointsSample = listOf(
            GpxPoint(
                lat = 35.09969000704587,
                lng = 129.12371004000306,
                elevation = 12.064,
                segmentDistance = 0.0
            ),
            GpxPoint(
                lat = 35.09968003258108,
                lng = 129.1236699745059,
                elevation = 12.028,
                segmentDistance = 3.8099458147524974
            ),
            GpxPoint(
                lat = 35.09969000704587,
                lng = 129.12363996729255,
                elevation = 12.316,
                segmentDistance = 2.9465950787147435
            ),
            GpxPoint(
                lat = 35.09973996318876,
                lng = 129.12359998561442,
                elevation = 13.189,
                segmentDistance = 6.639774050152178
            ),
            GpxPoint(
                lat = 35.099780028685934,
                lng = 129.12356997840106,
                elevation = 13.593,
                segmentDistance = 5.224942305886153
            ),
            GpxPoint(
                lat = 35.09983995929362,
                lng = 129.12352999672294,
                elevation = 14.148,
                segmentDistance = 7.592009941465822
            ),
            GpxPoint(
                lat = 35.09989997372031,
                lng = 129.12346998229623,
                elevation = 14.595,
                segmentDistance = 8.622179883197658
            ),
            GpxPoint(
                lat = 35.09994003921746,
                lng = 129.12342002615333,
                elevation = 14.813,
                segmentDistance = 6.364134913273343
            ),
            GpxPoint(
                lat = 35.099989995360374,
                lng = 129.1234300006181,
                elevation = 15.263,
                segmentDistance = 5.628497562349763
            ),
            GpxPoint(
                lat = 35.100326864048846,
                lng = 129.12342396564782,
                elevation = 19.883,
                segmentDistance = 37.46211242883706
            ),
            GpxPoint(
                lat = 35.10063003748655,
                lng = 129.1234300006181,
                elevation = 24.361,
                segmentDistance = 33.71581856021777
            ),
            GpxPoint(
                lat = 35.10088996030392,
                lng = 129.12343997508287,
                elevation = 24.741,
                segmentDistance = 28.916339602663438
            ),
            GpxPoint(
                lat = 35.10094997473062,
                lng = 129.1234300006181,
                elevation = 24.07,
                segmentDistance = 6.734709986729985
            ),
            GpxPoint(
                lat = 35.10100998915731,
                lng = 129.12340996786952,
                elevation = 23.477,
                segmentDistance = 6.917674099764088
            ),
            GpxPoint(
                lat = 35.10103999637068,
                lng = 129.12339001893997,
                elevation = 23.261,
                segmentDistance = 3.7982599818481795
            ),
            GpxPoint(
                lat = 35.10107000358401,
                lng = 129.1233699861914,
                elevation = 23.091,
                segmentDistance = 3.801908918356554
            ),
            GpxPoint(
                lat = 35.101130018010736,
                lng = 129.1233200300485,
                elevation = 22.925,
                segmentDistance = 8.07383472919034
            ),
            GpxPoint(
                lat = 35.10120998136699,
                lng = 129.1231900267303,
                elevation = 23.539,
                segmentDistance = 14.796333543952091
            ),
            GpxPoint(
                lat = 35.101360017433755,
                lng = 129.12297000177205,
                elevation = 26.928,
                segmentDistance = 26.057270358498318
            ),
            GpxPoint(
                lat = 35.10142279788853,
                lng = 129.12289515137672,
                elevation = 28.817,
                segmentDistance = 9.751897614457633
            ),
            GpxPoint(
                lat = 35.10150820948183,
                lng = 129.12293479777873,
                elevation = 29.401,
                segmentDistance = 10.159128675422513
            ),
            GpxPoint(
                lat = 35.10159823112191,
                lng = 129.12296799011528,
                elevation = 29.997,
                segmentDistance = 10.455476456553152
            ),
            GpxPoint(
                lat = 35.10163863189519,
                lng = 129.12298165261745,
                elevation = 30.24,
                segmentDistance = 4.661129762010676
            ),
            GpxPoint(
                lat = 35.1016701478511,
                lng = 129.12284578196704,
                elevation = 33.62,
                segmentDistance = 12.847640518303486
            ),
            GpxPoint(
                lat = 35.10163226164878,
                lng = 129.12282549776137,
                elevation = 33.393,
                segmentDistance = 4.599176426049247
            ),
            GpxPoint(
                lat = 35.10157107375562,
                lng = 129.12278979085386,
                elevation = 33.05,
                segmentDistance = 7.53944155884495
            ),
            GpxPoint(
                lat = 35.101578785106526,
                lng = 129.12273111753166,
                elevation = 33.807,
                segmentDistance = 5.4060840198341005
            ),
            GpxPoint(
                lat = 35.101590100675836,
                lng = 129.1226733662188,
                elevation = 34.477,
                segmentDistance = 5.402339050562698
            ),
            GpxPoint(
                lat = 35.1015977282077,
                lng = 129.1225095000118,
                elevation = 35.931,
                segmentDistance = 14.93139671302791
            ),
            GpxPoint(
                lat = 35.10164466686547,
                lng = 129.12243372760713,
                elevation = 36.388,
                segmentDistance = 8.646246117061693
            ),
            GpxPoint(
                lat = 35.10170878842474,
                lng = 129.12237010896206,
                elevation = 37.513,
                segmentDistance = 9.18326130740445
            ),
            GpxPoint(
                lat = 35.10165933519601,
                lng = 129.12226349115372,
                elevation = 35.975,
                segmentDistance = 11.149620943307795
            ),
            GpxPoint(
                lat = 35.101619269698844,
                lng = 129.1222576238215,
                elevation = 34.999,
                segmentDistance = 4.486941334099749
            ),
            GpxPoint(
                lat = 35.10158649645745,
                lng = 129.12222585640848,
                elevation = 34.004,
                segmentDistance = 4.6510391856970585
            ),
            GpxPoint(
                lat = 35.10156461969017,
                lng = 129.12210104987025,
                elevation = 31.959,
                segmentDistance = 11.611610093088121
            ),
            GpxPoint(
                lat = 35.10155908763409,
                lng = 129.12205771543086,
                elevation = 31.284,
                segmentDistance = 3.9899395574540177
            ),
            GpxPoint(
                lat = 35.10155556723475,
                lng = 129.12201354280114,
                elevation = 30.647,
                segmentDistance = 4.037509418257418
            ),
            GpxPoint(
                lat = 35.10152396723475,
                lng = 129.12172724280114,
                elevation = 26.256,
                segmentDistance = 26.281342524372825
            ),
            GpxPoint(
                lat = 35.1014922838658,
                lng = 129.12144080735743,
                elevation = 21.865,
                segmentDistance = 26.294804049910788
            ),
            GpxPoint(
                lat = 35.10127301327884,
                lng = 129.12147609516978,
                elevation = 18.559,
                segmentDistance = 24.592205465346556
            ),
            GpxPoint(
                lat = 35.10124996304512,
                lng = 129.12132002413273,
                elevation = 17.624,
                segmentDistance = 14.427690921252568
            ),
            GpxPoint(
                lat = 35.10119103826582,
                lng = 129.12104710936546,
                elevation = 17.904,
                segmentDistance = 25.67781719114949
            ),
            GpxPoint(
                lat = 35.10115834884345,
                lng = 129.12077612243593,
                elevation = 19.924,
                segmentDistance = 24.918970555228274
            ),
            GpxPoint(
                lat = 35.101130018010736,
                lng = 129.1204100009054,
                elevation = 26.345,
                segmentDistance = 33.45575460449211
            ),
            GpxPoint(
                lat = 35.10111995972693,
                lng = 129.11995000205934,
                elevation = 34.202,
                segmentDistance = 41.86234565761518
            ),
            GpxPoint(
                lat = 35.10110998526217,
                lng = 129.11964003928006,
                elevation = 41.486,
                segmentDistance = 28.22000221854617
            ),
            GpxPoint(
                lat = 35.10110998526217,
                lng = 129.11931003928007,
                elevation = 48.915,
                segmentDistance = 30.02104351484702
            ),
            GpxPoint(
                lat = 35.10110998526217,
                lng = 129.1189799644053,
                elevation = 56.344,
                segmentDistance = 30.027855087351764
            ),
            GpxPoint(
                lat = 35.10110998526217,
                lng = 129.1185699644053,
                elevation = 58.1605,
                segmentDistance = 37.29887224563141
            ),
            GpxPoint(
                lat = 35.10110998526217,
                lng = 129.11815996281803,
                elevation = 59.977,
                segmentDistance = 37.29901664645301
            ),
            GpxPoint(
                lat = 35.101160025224075,
                lng = 129.11790004000068,
                elevation = 58.2,
                segmentDistance = 24.29175757480552
            ),
            GpxPoint(
                lat = 35.10123998858035,
                lng = 129.1174500156194,
                elevation = 56.657,
                segmentDistance = 41.894383852676526
            ),
            GpxPoint(
                lat = 35.10130997747184,
                lng = 129.11705003120005,
                elevation = 63.304,
                segmentDistance = 37.21057861698746
            ),
            GpxPoint(
                lat = 35.10142487747184,
                lng = 129.11679003120005,
                elevation = 69.2735,
                segmentDistance = 26.882930710976044
            ),
            GpxPoint(
                lat = 35.10153997689486,
                lng = 129.1165300179273,
                elevation = 75.243,
                segmentDistance = 26.894509146628845
            ),
            GpxPoint(
                lat = 35.10176000185311,
                lng = 129.11617998965085,
                elevation = 80.758,
                segmentDistance = 40.156398554191114
            ),
            GpxPoint(
                lat = 35.10192001238466,
                lng = 129.1159100085497,
                elevation = 79.725,
                segmentDistance = 30.328165831885872
            ),
            GpxPoint(
                lat = 35.10202998295426,
                lng = 129.11578997969627,
                elevation = 78.988,
                segmentDistance = 16.393846221627875
            ),
            GpxPoint(
                lat = 35.10218001902104,
                lng = 129.11565997637808,
                elevation = 77.8,
                segmentDistance = 20.44993814280345
            ),
            GpxPoint(
                lat = 35.10229996405542,
                lng = 129.11557003855705,
                elevation = 77.123,
                segmentDistance = 15.646873893699382
            ),
            GpxPoint(
                lat = 35.10238998569548,
                lng = 129.11555000580847,
                elevation = 77.684,
                segmentDistance = 10.174490951033645
            ),
            GpxPoint(
                lat = 35.10247003287078,
                lng = 129.1155400313437,
                elevation = 78.334,
                segmentDistance = 8.94697195135373
            ),
            GpxPoint(
                lat = 35.1027000322938,
                lng = 129.1155400313437,
                elevation = 78.624,
                segmentDistance = 25.574768970992302
            ),
            GpxPoint(
                lat = 35.1031249322938,
                lng = 129.1155200313437,
                elevation = 75.712,
                segmentDistance = 47.28174318003938
            ),
            GpxPoint(
                lat = 35.103550041094415,
                lng = 129.11549996584654,
                elevation = 72.8,
                segmentDistance = 47.305172734272126
            ),
            GpxPoint(
                lat = 35.10380494109442,
                lng = 129.11549496584655,
                elevation = 75.3075,
                segmentDistance = 28.347236219584822
            ),
            GpxPoint(
                lat = 35.104059996083365,
                lng = 129.11548999138176,
                elevation = 77.815,
                segmentDistance = 28.364430808837234
            ),
            GpxPoint(
                lat = 35.10426996275783,
                lng = 129.115480016917,
                elevation = 80.21,
                segmentDistance = 23.364854451269675
            ),
            GpxPoint(
                lat = 35.10457003489135,
                lng = 129.115480016917,
                elevation = 83.952,
                segmentDistance = 33.36649887480938
            ),
            GpxPoint(
                lat = 35.104629965499036,
                lng = 129.11546995863318,
                elevation = 84.103,
                segmentDistance = 6.726502274567734
            ),
            GpxPoint(
                lat = 35.10470003820957,
                lng = 129.11544003523886,
                elevation = 83.875,
                segmentDistance = 8.253536973737468
            ),
            GpxPoint(
                lat = 35.10481000877919,
                lng = 129.1153500135988,
                elevation = 83.868,
                segmentDistance = 14.717012786917483
            ),
            GpxPoint(
                lat = 35.105000026524074,
                lng = 129.1151599958539,
                elevation = 84.88,
                segmentDistance = 27.29887948875227
            ),
            GpxPoint(
                lat = 35.105270007625215,
                lng = 129.11486998200417,
                elevation = 86.035,
                segmentDistance = 39.96556801351925
            ),
            GpxPoint(
                lat = 35.105454907625216,
                lng = 129.11467498200417,
                elevation = 82.094,
                segmentDistance = 27.154660792396065
            ),
            GpxPoint(
                lat = 35.105639984831214,
                lng = 129.1144799720496,
                elevation = 78.153,
                segmentDistance = 27.1701478289847
            ),
            GpxPoint(
                lat = 35.10586000978946,
                lng = 129.1142399981618,
                elevation = 72.14,
                segmentDistance = 32.78889465744986
            ),
            GpxPoint(
                lat = 35.105939973145716,
                lng = 129.11414000205696,
                elevation = 70.669,
                segmentDistance = 12.720202754068074
            ),
            GpxPoint(
                lat = 35.10605002753437,
                lng = 129.11398996599019,
                elevation = 69.724,
                segmentDistance = 18.33125085238473
            ),
            GpxPoint(
                lat = 35.10632998310029,
                lng = 129.11358000710607,
                elevation = 68.356,
                segmentDistance = 48.57785398761684
            ),
            GpxPoint(
                lat = 35.10653498310029,
                lng = 129.11329500710607,
                elevation = 66.6035,
                segmentDistance = 34.521666819826265
            ),
            GpxPoint(
                lat = 35.10674002580345,
                lng = 129.1130100376904,
                elevation = 64.851,
                segmentDistance = 34.522664284877564
            ),
            GpxPoint(
                lat = 35.106810014694915,
                lng = 129.11291004158556,
                elevation = 63.674,
                segmentDistance = 11.971155409388231
            ),
            GpxPoint(
                lat = 35.106880003586404,
                lng = 129.1128200199455,
                elevation = 62.867,
                segmentDistance = 11.29711462532436
            ),
            GpxPoint(
                lat = 35.10703003965319,
                lng = 129.11263997666538,
                elevation = 60.417,
                segmentDistance = 23.378744256824746
            ),
            GpxPoint(
                lat = 35.10710002854465,
                lng = 129.11255003884435,
                elevation = 59.139,
                segmentDistance = 11.291572904073867
            ),
            GpxPoint(
                lat = 35.107179991900935,
                lng = 129.11244995892048,
                elevation = 58.52,
                segmentDistance = 12.725557390837775
            ),
            GpxPoint(
                lat = 35.107196839526296,
                lng = 129.11239363253117,
                elevation = 58.011,
                segmentDistance = 5.45552442678791
            ),
            GpxPoint(
                lat = 35.10717001743614,
                lng = 129.11234996281564,
                elevation = 56.9,
                segmentDistance = 4.967459142608792
            ),
            GpxPoint(
                lat = 35.107214106246815,
                lng = 129.1122642159462,
                elevation = 56.458,
                segmentDistance = 9.212757860928047
            ),
            GpxPoint(
                lat = 35.107223493978374,
                lng = 129.1122089792043,
                elevation = 55.938,
                segmentDistance = 5.131953497153779
            ),
            GpxPoint(
                lat = 35.10721997357906,
                lng = 129.11210998892784,
                elevation = 55.149,
                segmentDistance = 9.013257732952093
            ),
            GpxPoint(
                lat = 35.107204634696245,
                lng = 129.11200588569045,
                elevation = 54.076,
                segmentDistance = 9.622231715391834
            ),
            GpxPoint(
                lat = 35.10727001354099,
                lng = 129.11198996007442,
                elevation = 55.676,
                segmentDistance = 7.412734590953395
            ),
            GpxPoint(
                lat = 35.107310498133316,
                lng = 129.11195433698595,
                elevation = 56.601,
                segmentDistance = 5.546701725164679
            ),
            GpxPoint(
                lat = 35.10732466354967,
                lng = 129.11194117739797,
                elevation = 56.94,
                segmentDistance = 1.9783824849848182
            ),
            GpxPoint(
                lat = 35.10737998411061,
                lng = 129.1118400078267,
                elevation = 58.433,
                segmentDistance = 11.069514320402412
            ),
            GpxPoint(
                lat = 35.107389958575354,
                lng = 129.11173003725708,
                elevation = 58.687,
                segmentDistance = 10.064863507672836
            ),
            GpxPoint(
                lat = 35.10737998411061,
                lng = 129.11154999397695,
                elevation = 58.009,
                segmentDistance = 16.415302794044667
            ),
            GpxPoint(
                lat = 35.107377637177706,
                lng = 129.1114303842187,
                elevation = 57.677,
                segmentDistance = 10.883534071703444
            ),
            GpxPoint(
                lat = 35.107373697683215,
                lng = 129.1113189049065,
                elevation = 57.981,
                segmentDistance = 10.150269050150328
            ),
            GpxPoint(
                lat = 35.10737000964582,
                lng = 129.1111999657005,
                elevation = 58.724,
                segmentDistance = 10.827177535634712
            ),
        )
    }
}