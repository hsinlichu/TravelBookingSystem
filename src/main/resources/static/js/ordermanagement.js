(function($) {

    'use strict';

    $.getJSON('/GetAllOrder', function (data) {
        console.log(data);
        $("#ordertable tr").remove();
        var template = `
        <tr data-id="%(id)s">
            <td scope="row">%(id)s</td>
            <td>%(trip.title)s</td>
            <td>%(trip.travelCodeName)s</td>
            <td>%(trip.startDate)s</td>
            <td>%(trip.endDate)s</td>
            <td>%(quantity)s</td>
            <td>%(trip.remainSits)s</td>
            <td>%(totalPrice)s</td>
        </tr>
        `;
        var i;
        for(i = 0;i < data.length; ++i){
            var tmp = sprintf(template, data[i]);
            //console.log(tmp);
            $("#ordertable").append(tmp);
        }
        
        $("#wholeordertable").Tabledit({
            url: '/editOrder',
            deleteButton: true,
            editButton: true,
            restoreButton: false,
            buttons: {
                delete: {
                    class: 'btn btn-sm btn-danger',
                    html: 'DELETE',
                    action: 'delete'
                },
                confirm: {
                    class: 'btn btn-sm btn-default',
                    html: 'Are you sure?'
                },
                edit: {
                    class: 'btn btn-sm btn-success',
                    html: 'EDIT',
                    action: 'edit'
                },
            },
            
            columns: {
            	
              identifier: [0, "id"],
              editable: [
            	  [5, 'quantity']
              ]
            },
           onSuccess: function(data, textStatus, jqXHR) {
                console.log('onSuccess(data, textStatus, jqXHR)');
                console.log(data);
                alert(data["msg"]);
                console.log(textStatus);
                console.log(jqXHR);
                location.reload();
            },
            onFail: function(jqXHR, textStatus, errorThrown) {
                console.log('onFail(jqXHR, textStatus, errorThrown)');
                console.log(jqXHR);
                console.log(textStatus);
                console.log(errorThrown);
                alert(errorThrown);
                location.reload();
            },
            onAlways: function() {
                console.log('onAlways()');
            },
            onAjax: function(action, serialize) {
                console.log('onAjax(action, serialize)');
                console.log(action);
                console.log(serialize);
            }
          });
    });
    

    $(function() {
      
    });

})(jQuery);




