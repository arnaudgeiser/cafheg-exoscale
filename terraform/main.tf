# Define the Exoscale Terraform Provider plugin
terraform {
  required_providers {
    exoscale = {
      source  = "exoscale/exoscale"
      version = "0.31.2"
    }
  }
}

# IAM API Key
variable "exoscale_api_key" {
  type = string
}

# IAM Secret Key
variable "exoscale_api_secret" {
  type = string
}

# Define some constants
locals {
  zone = "ch-gva-2"
}

# Define which template we want to use
data "exoscale_compute_template" "ubuntu" {
  zone = local.zone
  name = "Linux Ubuntu 20.04 LTS 64-bit"

}

# Define the default security group
data "exoscale_security_group" "default" {
  name = "default"
}

# Configure the Exoscale Terraform provider with API/secret key
provider "exoscale" {
  key = var.exoscale_api_key
  secret = var.exoscale_api_secret
}

# Register an SSH-keypair
resource "exoscale_ssh_key" "arnaudgeiser" {
  name = "arnaud-heg"
  public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQDjxtdCtyL/61DKNpZBKBShc7edc/wE9T60CvcOdEp427gH0IGGawBxBljFAFM5/x9+93S8G8mtD8146K7G4A33znxipl7oYBdej/q5rBi2mA3UjBTtM37oA5jjkpuyYOhj+2n7xLj9YQ58hcVg8yXHeg5VxlkxhiG1vlMDDlwI3NxjNrWVeVytCFrHqv+3dZuW5jRLYn7zkzWc7kri8ahXHeyrUtYpdm3D1GMUziP1t1izmVB8Hn2LEdCXER82mXExF148WkPNGJRHKAzDACEWau36fncjipEGh/ZYkPVXLkAvRpGxGcf3DvLqnNO8EVzRio2ew9ZgCHXiqWi4iV2icezpoPDgVGkJ54t93MmoJ3bXSETCUV4JwZVgWyXd7G0ltt2QBTWZ7wSpT+6yZmA/arC5rH5LKia1T95slZo7mWVstSyRaarw1Y71VD3d/0aXhIxqLkk8KrzgoFXkMGwMePwOuZ19/rfsI2wS7Si9qOs0zKWTOrXOCbvN+euthP5n8f4K82jJpGuPmwOBCw2QDe91ysRAifprPSM0zC/WQaRwEZuE5jwjQuhif29op60Ek39ieBypk5VWbw8rW64QYtCowcr+VGXVzQ85X6eBGuWlX/8d7A6l4L3I5rSVyWd3L0IkYS9hDedko37eLbQX7LUi95Ddu7LJ9bSl6SDqcQ=="
}

# Create a compute instance
resource "exoscale_compute_instance" "cafheg" {
  zone               = "ch-gva-2"
  name               = "cafheg"
  type               = "standard.medium"
  template_id        = data.exoscale_compute_template.ubuntu.id
  disk_size          = 10
  security_group_ids = [
    data.exoscale_security_group.default.id
  ]
  ssh_key            = exoscale_ssh_key.arnaudgeiser.name
}


