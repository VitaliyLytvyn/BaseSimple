package com.me.basesimple.global.extention


/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    class NetworkConnection : Failure()
    class ServerError(val cause: String? = null) : Failure()
    class OtherError(val cause: String? = null) : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()

    class NoSuchDocument: FeatureFailure()
}
