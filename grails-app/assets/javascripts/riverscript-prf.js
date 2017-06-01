/**
 * Created by ivanildo.junior on 19/04/2017.
 */


/* RiveScript.com "Try Online" Script */
window.bot = null;

var scriptBot = null;

function loadTemplate(template) {
    // Get the template.
    $.ajax({
        url: "../assets/prf/" + template,
        dataType: "text",
        error: function(jqXHR, textStatus, error) {
            window.alert(error);
        },
        success: function(data, textStatus, jqXHR) {
            scriptBot = data;

        }
    });
}

$(document).ready(function() {
    // Show the version number being used.
    console.log("RiveScript-JS version " + new RiveScript().version());


    loadTemplate("basico.rive");

    // The execute button!
    $("#iniciar").on('click',function() {
        // Get their code.
        var code = scriptBot;
        if (code.length == 0) {
            window.alert("You didn't enter any RiveScript code!");
            return false;
        }

        // Initialize the bot.
        window.bot = new RiveScript();
        window.bot.setHandler("coffeescript", new RSCoffeeScript(window.bot));
        window.bot.stream(code, function(error) {
            window.alert("Error in your RiveScript code:\n\n" + error);
        });
        window.bot.sortReplies();

        // Reset the dialogue.
        $("#historico").empty();

        $("#modalContato").modal();
    });

    // Modal events
    // $("#modalContato").on("shown.bs.modal", function() {
    //     $("#pergunta").focus();
    // });
    $("#modalContato").on("hidden.bs.modal", function() {
        // Unload the RiveScript bot to clean up memory.
        window.bot = null;
    });

    // The Enter key.
    $("#pergunta").keydown(function(e) {
        if (e.keyCode == 13) {

            //Código próprio

            // var indexDoubleDot = $('#pergunta').val().indexOf(':');
            // var nomeUsuario = $('#pergunta').val().substring(0,indexDoubleDot) + ": ";
            // var textarea = $('#historico');
            // var historico = textarea.val();
            //
            // var pergunta = $('#pergunta').val();
            //
            // historico = historico + pergunta + '\n';
            // pergunta = nomeUsuario;
            // textarea.val(historico);
            //
            // $('#pergunta').val(pergunta);
            // textarea.scrollTop(textarea[0].scrollHeight);

            //Fim código próprio


            var $dialogue = $("#historico");
            var $message = $("#pergunta");

            if (window.bot === null) {
                return; // No bot? Weird.
            }

            var message = $message.val();
            if (message.length == 0) {
                return;
            }
            var reply = window.bot.reply("local-user", message);
            reply = reply.replace(new RegExp("\n", "g"), "<br>");

            // Update the dialogue.
            var $user = $("<div></div>");
            var $bot = $("<div></div>");
            $user.html('<span class="user">User:</span> ' + message);
            $bot.html('<span class="bot">Bot:</span> ' + reply);
            $dialogue.append($user);
            $dialogue.append($bot);

            // Scroll to bottom.
            $dialogue.animate({ scrollTop: $dialogue[0].scrollHeight }, 1000);

            // Clear the input.
            $message.val("");
        }
    })
});