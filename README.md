# Heraldic Markup Language

This project attempts to define an XML schema for describing heraldic *coats of arms*. The first focus is on detailing
and rendering the escutcheon (the shield), in later stages this may be expanded to descriptions of the rest of the
heraldic *achievement*.

## Example

The following fragment of XML code:

    <?xml version="1.0" encoding="UTF-8"?>
    <coatOfArms xmlns="http://github.com/wlievens/heraldry" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://github.com/wlievens/heraldry ../src/main/resources/xsd/heraldry.xsd">
        <shape>heater-shield</shape>
        <background>
            <division type="quarterly">
                <part position="1,3">
                    <background>
                        <field tincture="or"/>
                    </background>
                </part>
                <part position="2,4">
                    <background>
                        <field tincture="argent"/>
                    </background>
                </part>
            </division>
        </background>
        <charges>
            <ordinary type="fret" tincture="gules"/>
        </charges>
    </coatOfArms>

Can be turned into the following image:

![Shield with Coat of Arms](https://cdn.rawgit.com/wlievens/hml/master/examples/output/example018.svg)

As well as generate the following description in blazon:

*Quarterly or and argent a fret gules*