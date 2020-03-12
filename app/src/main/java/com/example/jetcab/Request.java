package com.example.jetcab;

public class Request implements RequestResponse {

    //private RideInfo rideInfo;
    private String status;

    /**
     *  create a new Request and will save it in the firebase
     *  Initially set the request to Open
     */

    public Request( /*PickUpLocation, DropoffLocation*/)
    {
        ResponseOpenRequest ();
        // save the request in the firbase along with ride info

    }

    /**
     * Set the status open, will be used to display all open request to the driver
     */
    @Override
    public void ResponseOpenRequest () {
        status="Open";
    }

    /**
     * Set the status  cancelled, if the rider cancelled the ride;
     */
    @Override
    public void CancelledRequest () {
        status="Cancelled";
    }

    /**
     * Set the status  completed after the rider has reached his destination;
     */
    @Override
    public void CompletedRequest () {
        status="Completed";
    }

    /**
     * Set the status Accepted if the driver accepted the riders request;
     */
    @Override
    public void AcceptedRequest () {
        status="Accepted";
    }
}
