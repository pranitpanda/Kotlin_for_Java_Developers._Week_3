package nicestring

fun String.isNice(): Boolean {

    var vowelCount = 0
    var prevLetter = '_'
    val conditionsMet : MutableList<Int> = mutableListOf(1,0,0)
    for (c in this) {
        if (c in "aeiou"){
            vowelCount++
            if (vowelCount == 3){
                conditionsMet[1] = 1
            }
            if (c in "uae" && prevLetter == 'b'){
                conditionsMet[0] = 0
            }
        }
        if (prevLetter == c){
            conditionsMet[2] = 1
        }
        prevLetter = c
    }
    return conditionsMet.sum() >=2
}