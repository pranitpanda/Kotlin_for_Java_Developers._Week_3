package taxipark

import java.lang.Double.sum
import kotlin.math.floor
import kotlin.math.min

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val fakeDrivers: MutableSet<Driver> = this.allDrivers.toMutableSet()
    for (trip in this.trips) {
        if (trip.driver in fakeDrivers) {
            fakeDrivers.remove(trip.driver)
        }
    }
    return fakeDrivers
}


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    val rideCounts : MutableMap<Passenger, Int> = mutableMapOf()
    val faithfulPassengers: MutableSet<Passenger> = mutableSetOf()
    if (minTrips == 0){
        return this.allPassengers
    }
    for (trip in this.trips) {
        for (passenger in trip.passengers) {
            rideCounts[passenger] = rideCounts.getOrDefault(passenger, 0) + 1
            if (rideCounts[passenger] == minTrips){
                faithfulPassengers.add(passenger)
            }
        }
    }
    return faithfulPassengers
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
//    val passengers: MutableSet<Passenger> = mutableSetOf()
//    val answer: MutableSet<Passenger> = mutableSetOf()
//    for (trip in this.trips) {
//        if (trip.driver == driver){
//            for (passenger in trip.passengers){
//                if (passenger in passengers){
//                    answer.add(passenger)
//                }else{
//                    passengers.add(passenger)
//                }
//            }
//        }
//    }
//    return answer
    val trip_counts : MutableMap<Passenger, Int> = mutableMapOf()
    for (trip in this.trips) {
        if (trip.driver == driver) {
            for (passenger in trip.passengers) {
                trip_counts[passenger] = trip_counts.getOrDefault(passenger, 0) + 1
            }
        }
    }
    return trip_counts.filterValues { it > 1 }.keys

}
/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val numTrips: MutableMap<Passenger, Int> = mutableMapOf()
    val numDiscounts: MutableMap<Passenger, Int> = mutableMapOf()
    for (trip in this.trips) {
        if (trip.discount != null && trip.discount > 0){
            for (passenger in trip.passengers) {
                numDiscounts[passenger] = numDiscounts.getOrDefault(passenger, 0) + 1
            }
        }
        for (passenger in trip.passengers) {
            numTrips[passenger] = numTrips.getOrDefault(passenger, 0) + 1
        }
    }
    return numTrips.filter { (passenger, trips) -> (numDiscounts.getOrDefault(passenger, 0) / trips.toFloat()) > 0.5 }.keys
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val counts: MutableMap<IntRange, Int> = mutableMapOf()
    for (trip in this.trips) {
        val floored = (trip.duration / 10) * 10
        counts[floored..(floored +9)] = counts.getOrDefault(floored..(floored +9), 0) + 1
    }
    return counts.maxByOrNull { it.value }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val driversIncomes : MutableMap<Driver, Double> = mutableMapOf()
    var tot_income = 0.0

    if (this.trips.isEmpty()){
        return false
    }

    for (trip in this.trips) {
        driversIncomes[trip.driver] = driversIncomes.getOrDefault(trip.driver, 0.0) + trip.cost
        tot_income += trip.cost
    }

    val eligibleDrivers : Int = floor(this.allDrivers.size*0.2).toInt()
    val sortedDescending = driversIncomes.values.sortedDescending()
    var sumsofar = 0.0
    for (i in 1..(min(sortedDescending.size, eligibleDrivers))){
        sumsofar += sortedDescending[i-1]
    }
    return sumsofar >= 0.8*tot_income
}