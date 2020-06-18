(function($) {

    'use strict';
    var trip_array;


    $.getJSON('/GetAllTrip', function (data) {
    	console.log(data);
        trip_array = data;
        
        $(".tripGallery .trip").remove();
        var template = `
        <div class="col-lg-12 col-md-12 trip">
            <div class="single_place row">
                <div class="thumb col-md-6" style="padding-left: 0px;">
                    <img src="img/place/%(pic)s.png" alt="">
                    <a href="/trip?id=%(id)s" class="prise">$%(price)s</a>
                </div>
                <div class="place_info col-md-6">
                    <a href="/trip?id=%(id)s"><h4>%(title)s</h4></a>
                    <p>%(travelCodeName)s</p>
                    <span>Start date: %(startDate)s</span></br>
                    <span>End date: %(endDate)s</span></br>
                    <span>Remained sit: TBA</span></br>

                    <div class="rating_days d-flex justify-content-between">
                        <div class="days">
                            <i class="fa fa-clock-o"></i>
                            <a href="/trip?id=%(id)s">5 Days</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        `;
        
        var empty = `
        <div class="col-lg-12 col-md-12 trip">
            <div class="single_place row">
                <div class="thumb col-md-6" style="padding-left: 0px;">
                    <img src="img/noresult.png" alt="noresult">
                </div>
                <div class="d-flex place_info col-md-6 align-items-center">
                	<h3 class="mx-auto">Search no result QQ</h3>
                </div>
            </div>
        </div>
        `
        
        var i;
        for(i = 0;i < data.length; ++i){
        	data[i]["pic"] = Math.floor(Math.random() * 6 +1);
        	
            var tmp = sprintf(template, data[i]);
            $(".tripGallery").append(tmp);
        }
        if(data.length == 0)
        	$(".tripGallery").append(empty);

        //$.each(data, function (key, value) {

        //});
        //
       
    });

    
})(jQuery);





