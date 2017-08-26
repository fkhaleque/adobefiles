var gov = gov || {};
gov.hc = gov.hc || {};

// Sidr Navigation
gov.hc.sidr = {
  init: function() {
    
    // Attach mobile flyout click command
    $('#hc-gov-assets .mobile-menu-btn').on('click.show', gov.hc.sidr.showMobileNav);

    // on click, update section colors
    $('#hc-gov-assets #sidr .header-actions a').click(function (e) {
      
      e.preventDefault();

      var css = "visible-xs visible";

        switch ($(this).attr("data-tab-name"))
        {
            case "learn":

                css += " learn-menu";

            break;

            case "marketplace":

                css += " marketplace-menu"; 

            break;

        }

      $('#hc-gov-assets #sidr').attr("class",css);

    });
  },
  showMobileNav: function(e) {

    if (e) {
      e.preventDefault();
    }

    $('#hc-gov-assets .mobile-menu-btn').off('click.show');

    $('#hc-gov-assets #wrapper').addClass('pushed');

    var $nav = $('#hc-gov-assets #sidr');
    $nav.addClass('visible');

    _.defer(function() {
      $(document.body).on('click.mobilenav', function(e) {

        if (!$.contains($nav[0], e.target) && !$nav.is($(e.target))) {
          gov.hc.sidr.hideMobileNav();
          $nav = null;
        }
      });
    });

  },
  hideMobileNav: function() {
    $('#hc-gov-assets #wrapper').removeClass('pushed');
    $('#hc-gov-assets #sidr').removeClass('visible');

    $(document.body).off('click.mobilenav');
    $('#hc-gov-assets .mobile-menu-btn').on('click.show', gov.hc.sidr.showMobileNav);
  }
};

$(function() {
  gov.hc.sidr.init();
});

// Glossary Terms
gov.hc.glossaryTerms = {
  options_tooltip : {
    trigger: "hover focus",
    container: "#hc-gov-assets"
  },
  setAria: function(terms) {

    $(terms).each(function(){
        var id = new Date().getTime(),
			uid = 'tooltip'+id, //UUID Polyfill
            className = '',
            tooltipTemplate = '<div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>',
            popoverTemplate = '<div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>',
            $this = $(this);
        
        $this.attr({'aria-describedby': uid});

        // Polyfill 
        if(this.className.indexOf('tip') !== -1){
          
            className += 'left tooltip';
            gov.hc.glossaryTerms.options_tooltip.template = '<div aria-role="tooltip" class="'+ className + '" id="' + uid + '">' + tooltipTemplate;
            
            $(this).tooltip(gov.hc.glossaryTerms.options_tooltip);

        } else {
            
            className += 'popover popover-glossary';
            gov.hc.glossaryTerms.options_tooltip.template = '<div aria-role="tooltip" class="'+ className + '" id="' + uid + '">' + popoverTemplate;
            gov.hc.glossaryTerms.options_tooltip.placement = function (context, source) {
              var position = $(source).offset();
              var rtOffset = ($(window).width() - ($(source).offset().left + $(source).outerWidth()));
              var lftOffset = (position.left - $(window).scrollLeft());
              var topOffset = (position.top - $(window).scrollTop());
              var bottomOffset = ($(window).height() - topOffset);

              if (rtOffset > lftOffset) {
                  if (topOffset < 300) {
                      return "bottom";
                  } else if (bottomOffset < 300) {
                      return "top";
                  }
                  return "right";
              }
              else {
                  if (topOffset < 300) {
                      return "bottom";
                  } else if (bottomOffset < 300) {
                      return "top";
                  }
                  return "left";
              };
            }
            
          $(this).popover(gov.hc.glossaryTerms.options_tooltip);

        }

        // prevent link from firing natural event
        // and appending a "#" to the url.
        $this.click(function(event){
            event.preventDefault();
        });
        // for keyboard users - enter key prevent
        // link from firing natural event.
        // and appending a "#" to the url.
        $this.keypress(function(event) {
            if(event.which == '13') {
                event.preventDefault();
            }
        });
    });

  }  
};

$(function() {
  gov.hc.glossaryTerms.setAria(".glossary-term");
  gov.hc.glossaryTerms.setAria(".tip");
});

// Making Accordions Accessible
gov.hc.accordions = {
  setAria: function() {
    
    // On collapse open
    $(document).on('shown.bs.collapse', function(event) {
      
      var content = $(event.target);
      var id = content.attr("aria-describedby");

      // Set aria-expanded to true
      $('#' + id).attr('aria-expanded', true);
      
      // Set aria-hidden to false
      content.attr('aria-hidden', false);
     
      // Set focus on the first link in the shown urls
      setTimeout(function() {
        content.find('li:first-child a').focus();
      }, 10);
    });

    // On collapse close
    $(document).on('hidden.bs.collapse', function(event) {
      
      var content = $(event.target);
      var id = content.attr("aria-describedby");

      // Set aria-expanded to false
      $('#' + id).attr('aria-expanded', false);
      
      // Set aria-hidden to true
      content.attr('aria-hidden', true);

      // Set focus back to dropdown toggle
      $('#' + id).focus();

    });

  }
}

$(function() {
  gov.hc.accordions.setAria();
});