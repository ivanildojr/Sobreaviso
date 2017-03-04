// NOTICE!! DO NOT USE ANY OF THIS JAVASCRIPT
// IT'S ALL JUST JUNK FOR OUR DOCS!
// ++++++++++++++++++++++++++++++++++++++++++

!function ($) {

  $(function(){


		$("#menu-toggler").on("click",function(){$("#sidebar").toggleClass("display");$(this).toggleClass("display");return false});var a=false;$("#sidebar-collapse").on("click",function(){$("#sidebar").toggleClass("menu-min");$(this.firstChild).toggleClass("icon-double-angle-right");a=$("#sidebar").hasClass("menu-min");if(a){$(".open > .submenu").removeClass("open")}});$(".nav-list").on("click",function(d){if(a){return}var c=$(d.target).closest(".dropdown-toggle");if(c&&c.length>0){var b=c.next().get(0);if(!$(b).is(":visible")){$(".open > .submenu").each(function(){if(this!=b&&!$(this.parentNode).hasClass("active")){$(this).slideUp(200).parent().removeClass("open")}})}$(b).slideToggle(200).parent().toggleClass("open");return false}})

    var $window = $(window)

    // Disable certain links in docs
    $('section [href^=#]').click(function (e) { e.preventDefault(); });

		// botão de busca na barra de menú secundária
		$('.busca').click(function(){ $('.text-busca').slideToggle('slow'); return false; });

    // make code pretty
    window.prettyPrint && prettyPrint()

    // add-ons
    $('.add-on :checkbox').on('click', function () {
      var $this = $(this)
        , method = $this.attr('checked') ? 'addClass' : 'removeClass'
      $(this).parents('.add-on')[method]('active')
    })

    // add tipsies to grid for scaffolding
    if ($('#gridSystem').length) {
      $('#gridSystem').tooltip({
          selector: '.show-grid > [class*="span"]'
        , title: function () { return $(this).width() + 'px' }
      })
    }

		function menu (action) {
			if (action == 'toggle') {
				if ($('.nav-open').is(":visible")) {
					$('#main-content').css('margin-left','0');
					$('.breadcrumb').css('margin-left','190px');
				} else {
					$('#main-content').css('margin-left','190px');
					$('.breadcrumb').css('margin-left','0');
				}
				$('.nav-open').slideToggle('slow');
				$('.nav-close').slideToggle('slow');
			}
			if (action == 'close') {
				$('.nav-open').hide();
				$('.nav-close').show();
				$('#main-content').css('margin-left','0');
				$('.breadcrumb').css('margin-left','190px');
			}
			if (action == 'open') {
				$('.nav-open').show();
				$('.nav-close').hide();
				$('#main-content').css('margin-left','190px');
				$('.breadcrumb').css('margin-left','0');
			}

		}

		// Fechar barra lateral
		$('.fechar-sidebar').click(function(){
			menu('toggle');
			return false;
		});

		if ($('.nav-stacked.fix').length>0) {
			$(window).scroll(function () {
				var height = $(window).height();
				var scrollTop = $(window).scrollTop();
				if (scrollTop>180) $('.nav-stacked.fix').addClass('fixed');
										else	$('.nav-stacked.fix').removeClass('fixed');
			});
		}

		$('#frmContato').submit(function(){	$('#iframeContato').show('slow');	});

    // tooltip demo
    $('.tooltip-demo').tooltip({ selector: "a[data-toggle=tooltip]" })

    $('a[data-toggle=tooltip]').tooltip();

    $('.popover-test').popover()

    // popover demo
    $("a[data-toggle=popover]")
      .popover()
      .click(function(e) {
        e.preventDefault()
      })

	
		/*
		ALTERAÇÕES EM 13-03-2014
		+ ajuste de responsividade do menu lateral e da página em resoluções menores que 800 px
		Autor: Leonardo Zanette
		*/
    function menuResponsivo () {
      var w = $(window).width();
      if (w<800) { $('.btn-user').html('<i class="icon-user"></i>'); 
			$('.footer-links').html('<li><a href="#">Topo</a></li><li><a href="#modalContato" data-toggle="modal"> Fale Conosco</a></li>'); }
      if (w<600) { menu('close'); }
    }
    menuResponsivo();
    $(window).resize(function(){ menuResponsivo(); });
		/* FIM alteração 13-03-2014 */

    
  })

}(window.jQuery)
