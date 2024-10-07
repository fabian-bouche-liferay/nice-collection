package com.liferay.samples.fbo.asset.info.custom;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
        category = "custom-info-list",
        scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
		id = CustomInfoListRendererConfigurationConstants.CONFIGURATION_ID,
		localization = "content/Language",
		name = "custom-info-list-renderer-configuration"
		)
public interface CustomInfoListRendererConfiguration {

	@Meta.AD(
			deflt = "",
			description = "custom-tag-names-description",
			name = "custom-tag-names", required = false
	)
	public String[] customTagNames();
	
}
