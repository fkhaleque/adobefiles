var gov = gov || {};
gov.hc = gov.hc || {};

//guideRuntime.js function override
gov.hc.guideRuntime = function(){	

	var pdfDownloadModal = "#pdfDownload",

	submit = function (options) {
		var self = this;

		function submitSuccess(data) {

			var submitData = {};
			var guideContainerPath = guideBridge.getGuidePath();
			var actionUrl = guideBridge._getGuidePathUrl(".af.submit.jsp/" + getGuideShortName() + ".pdf");
			submitData['guideContainerPath'] = guideContainerPath;
			submitData['_asyncSubmit'] = false;
			submitData['_charset_'] = "UTF-8";
			var customPropMap = guidelib.runtime.guideContext.customPropertyMap || {};
			for (var prop in customPropMap) {
				if (customPropMap.hasOwnProperty(prop)) {
					submitData[prop] = customPropMap[prop];
				}
			}
			var resultJson = data,
			guideJson,
			dataXml,
			fileAttachmentsList,
			attachments = [],
			fileCount = 0;
			if (resultJson.hasOwnProperty("guideValue")) {
				guideJson = resultJson["guideValue"];
			}
			if (resultJson.hasOwnProperty("dataXml")) {
				dataXml = resultJson["dataXml"];
			}

			if (resultJson.hasOwnProperty("fileAttachmentsList")) {
				fileAttachmentsList = resultJson["fileAttachmentsList"];
			}

			for (var key in guideJson) {
				if (guideJson.hasOwnProperty(key)) {
					submitData[key] = guideJson[key];
				}
			}

			if (dataXml) {
				submitData['jcr:data'] = dataXml;
				submitData['jcr:data@TypeHint'] = "Binary";
			}

			var actionFieldDiv = $("#actionField");
			var actionFields = actionFieldDiv.find('input')
			.each(function () {
				submitData[$(this).attr("name")] = $(this).val();
				if ($(this).attr("name") == "_guideValueMap" && $(this).val() == "yes") {
					var guideKeyValue = {};
					self._guide._collectValues(guideKeyValue);
					submitData['_guideValuesMap'] = JSON.stringify(guideKeyValue);
				}
			});

			var allFiles = [];
			if (fileAttachmentsList.length > 0 && !guideBridge._disablePreview()) {
				_.each(fileAttachmentsList, function (fileUploadComponentId, index) {
					var currentCount = 0;
					var componentInModel = self._resolveId(fileUploadComponentId);
					var fileNameList = componentInModel["fileAttachment"].value;
					if (fileNameList) {
						var fileNames = fileNameList.split("\n");

						_.each(fileNames, function (fileUrl, index) {
							var name = componentInModel.name + "/" + fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
							attachments[fileCount++] = name;
							submitData["_guideFileAttachment." + name] = fileUrl;
						}, this);
					}
				}, this);
				submitData['_guideAttachments'] = attachments;

			}

			//Target = '_blank' is the concept at play to load PDF in new tab
			var $form = $("<form method='post' enctype='multipart/form-data' target='_blank'/>");
			$form.attr('action', actionUrl);


			for (var fields in submitData) {
				if (submitData.hasOwnProperty(fields)) {
					$("<input>").attr("type", "hidden")
					.attr("name", fields)
					.val(submitData[fields])
					.appendTo($form);
				}
			}
			/*
			 * This means we did not do any preprocessing
			 * for file attachments so fileAttachment list won't be present
			 * so posting DOM for the files.
			 * */

			if (guideBridge._disablePreview()) {
				var attachmentNameDomMap = guideBridge._getFileNameFileDomMap(),
				fileNameList = [],
				_guideAttachments= "";
				if (attachmentNameDomMap && attachmentNameDomMap.fileDom) {
					fileNameList = attachmentNameDomMap.fileName;
					_.each(attachmentNameDomMap.fileDom, function(file, index) {
						file.attr("name", fileNameList[index])
						.appendTo($form);
						_guideAttachments += fileNameList[index] + ",";

					});
				}
				$("<input>").attr("type", "hidden")
				.attr("name", "_guideAttachments")
				.attr("value", _guideAttachments)
				.appendTo($form);
			}

			$form.appendTo($("body"));
			$form.submit();
		}

		if (guideBridge.validate(null, null, true, false)) {
			logMessage("[gov.hc.guideRuntime] Form Validated");

			//Show #pdfDownload modal 
			$(pdfDownloadModal).modal("show");

			//Show spinner
			$(pdfDownloadModal).find("a").find("span").show();			

			// Trigger submitStart before calling internal submit.
			guideBridge._guide._triggerOnBridge("submitStart", "", "", "", "");
			guideBridge._submitInternal({success:submitSuccess,
				error: $.proxy(guideBridge._handleSubmitError, this),
				fileUploadPath: guideBridge.getTempPath()
			});

			logMessage("[gov.hc.guideRuntime] Form Submitted");

		} else {
			logMessage("[gov.hc.guideRuntime] Form Invalid");

			//Hide #pdfDownload modal 
			$(pdfDownloadModal).modal("hide");

			return;
		}

		//Hide spinner
		$(pdfDownloadModal).find("a").find("span").hide();

	},

	getGuideShortName = function(){
		var pathUrl = guideBridge._getGuidePathUrl();

		var guideName = pathUrl.substring(0,pathUrl.indexOf("jcr:content")-1);

		guideName = guideName.substring(guideName.lastIndexOf("/")+1, guideName.length);

		return guideName;
	},

	logMessage = function(msg){	
		try{
			console.log(msg);
		} catch (e){
			//Silently absorb failed console call
		}
	};

	return {
		submit:submit,
		getGuideShortName:getGuideShortName
	}
}();