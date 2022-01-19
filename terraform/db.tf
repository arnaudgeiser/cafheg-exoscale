variable "db_password" {
  type = string
  sensitive = true
}

resource "exoscale_compute_instance" "db" {
  zone               = "ch-gva-2"
  name               = "db"
  type               = "standard.medium"
  template_id        = data.exoscale_compute_template.ubuntu.id
  disk_size          = 10
  security_group_ids = [
    data.exoscale_security_group.default.id
  ]
  ssh_key            = exoscale_ssh_key.arnaudgeiser.name
  user_data          = <<EOF
#cloud-config
package_update: true
packages:
  - docker.io

runcmd:
  - "docker run --detach --net=host --name db --env MARIADB_USER=user --env MARIADB_PASSWORD=${var.db_password} --env MARIADB_ROOT_PASSWORD=${var.db_password} --env MARIADB_DATABASE=mydb mariadb:latest"
EOF
}
