/**
 * Created by oper0002 on 20/02/2017.
 */
function moveCard(ui, number, phase) {
  $.ajax({
           url: "/card",
           type: 'PUT',
           data: {number: number, phase: phase},
           error: function (xhr, status, errThrown) {
             $('#message').html(xhr.responseText)
             errorCallable(ui)
           }
         }).done( function (message) {
            $('#message').html(message)
         }
  );
}
function errorCallable(ui) {
  ui.draggable.animate(ui.draggable.data().uiDraggable.originalPosition,"slow");
}
$( function() {
  $(".story").draggable({
    refreshPositions: true,
  });
  $("#readyPhase").droppable({
                               drop: function (event, ui) {
                                 moveCard(ui, ui.draggable.attr("id"), 'ready')
                               }
                             });
  $("#devPhase").droppable({
                             drop: function (event, ui) {
                               moveCard(ui, ui.draggable.attr("id"), 'dev')
                             }
                           });
  $("#testPhase").droppable({
                              drop: function (event, ui) {
                                moveCard(ui, ui.draggable.attr("id"), 'test')
                              }
                            });
  $("#deployPhase").droppable({
                                drop: function (event, ui) {
                                  moveCard(ui, ui.draggable.attr("id"), 'deploy')
                                }
                              });
});