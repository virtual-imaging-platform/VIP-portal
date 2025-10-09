package fr.insalyon.creatis.vip.api.model;

import jakarta.validation.constraints.NotNull;

public class DeleteExecutionConfiguration {
    @NotNull
    private Boolean deleteFiles;

    public Boolean getDeleteFiles() {
        return deleteFiles;
    }

    public void setDeleteFiles(Boolean deleteFiles) {
        this.deleteFiles = deleteFiles;
    }
}
